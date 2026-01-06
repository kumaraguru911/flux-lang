package parser;

import ast.Expr;
import ast.Stmt;
import java.util.ArrayList;
import java.util.List;
import lexer.Token;
import lexer.TokenType;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
    List<Stmt> statements = new ArrayList<>();
    while (!isAtEnd()) {
        statements.add(declaration());
    }
    return statements;
}

    private Stmt declaration() {

    // ✅ 1️⃣ CLASS FIRST (highest priority)
    if (match(TokenType.CLASS)) return classDeclaration();

    // ✅ 2️⃣ Named functions
    if (check(TokenType.FUN) && checkNext(TokenType.IDENTIFIER)) {
        advance(); // consume 'fun'
        return functionDeclaration();
    }

    return statement();
}


    private Stmt statement() {
    if (match(TokenType.PRINT)) return printStatement();
    if (match(TokenType.EXIT)) return new Stmt.Exit();
    if (match(TokenType.BREAK)) return new Stmt.Break();
    if (match(TokenType.CONTINUE)) return new Stmt.Continue();
    if (match(TokenType.IF)) return ifStatement();
    if (match(TokenType.WHILE)) return whileStatement();
    if (match(TokenType.FOR)) return forStatement();
    if (match(TokenType.RETURN)) return returnStatement();
    return expressionStatement();
}


    private Stmt printStatement() {
    List<Expr> values = new ArrayList<>();
    values.add(expression());

    while (match(TokenType.COMMA)) {
        values.add(expression());
    }

    return new Stmt.Print(values);
}

    private Stmt expressionStatement() {
    Expr expr = expression();

    if (match(TokenType.EQUAL)) {
        Expr value = expression();

        if (expr instanceof Expr.Variable) {
            return new Stmt.Assignment(
                ((Expr.Variable) expr).name,
                value
            );
        }
        else if (expr instanceof Expr.Get) {
            Expr.Get get = (Expr.Get) expr;
            return new Stmt.Expression(
                new Expr.Set(get.object, get.name, value)
            );
        }

        throw error(peek(), "Invalid assignment target.");
    }

    return new Stmt.Expression(expr);
}

    private Stmt returnStatement() {
    Expr value = null;

    if (!check(TokenType.RIGHT_BRACE)) {
        value = expression();
    }

    return new Stmt.Return(value);
}

    private Stmt classDeclaration() {
    Token name = consume(TokenType.IDENTIFIER, "Expected class name.");
    consume(TokenType.LEFT_BRACE, "Expected '{' after class name.");

    List<Token> fields = new ArrayList<>();
    List<Stmt.Function> methods = new ArrayList<>();

    while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
        if (match(TokenType.FUN)) {
            methods.add((Stmt.Function) functionDeclaration());
        } else {
            fields.add(
                consume(TokenType.IDENTIFIER, "Expected field name.")
            );
        }
    }

    consume(TokenType.RIGHT_BRACE, "Expected '}' after class body.");
    return new Stmt.Class(name, fields, methods);
}



    private Stmt functionDeclaration() {
    Token name = consume(TokenType.IDENTIFIER, "Expected function name.");

    consume(TokenType.LEFT_PAREN, "Expected '(' after function name.");

    List<Token> parameters = new ArrayList<>();
    if (!check(TokenType.RIGHT_PAREN)) {
        do {
            parameters.add(
                consume(TokenType.IDENTIFIER, "Expected parameter name.")
            );
        } while (match(TokenType.COMMA));
    }

    consume(TokenType.RIGHT_PAREN, "Expected ')' after parameters.");
    consume(TokenType.LEFT_BRACE, "Expected '{' before function body.");

    List<Stmt> body = new ArrayList<>();
    while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
        body.add(declaration());
    }
    consume(TokenType.RIGHT_BRACE, "Expected '}' after function body.");

    return new Stmt.Function(name, parameters, body);
}

    private Expr call() {
    Expr expr = primary();

    while (true) {
        if (match(TokenType.LEFT_PAREN)) {
            expr = finishCall(expr);
        }
        else if (match(TokenType.DOT)) {
            Token name = consume(
                TokenType.IDENTIFIER,
                "Expected property name after '.'."
            );
            expr = new Expr.Get(expr, name);
        }
        else {
            break;
        }
    }

    return expr;
}



private Expr finishCall(Expr callee) {
    List<Expr> arguments = new ArrayList<>();

    if (!check(TokenType.RIGHT_PAREN)) {
        do {
            arguments.add(expression());
        } while (match(TokenType.COMMA));
    }

    consume(TokenType.RIGHT_PAREN, "Expected ')' after arguments.");

    return new Expr.Call(callee, arguments);
}


    private Stmt ifStatement() {
    Expr condition = expression();

    consume(TokenType.LEFT_BRACE, "Expected '{' after if condition.");

    List<Stmt> thenBody = new ArrayList<>();
    while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
        thenBody.add(statement());
    }
    consume(TokenType.RIGHT_BRACE, "Expected '}' after if body.");

    List<Stmt> elseBody = null;
    if (match(TokenType.ELSE)) {
        consume(TokenType.LEFT_BRACE, "Expected '{' after else.");
        elseBody = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            elseBody.add(statement());
        }
        consume(TokenType.RIGHT_BRACE, "Expected '}' after else body.");
    }

    return new Stmt.If(condition, thenBody, elseBody);
}

    private Stmt forStatement() {
    // for i = start to end { body }

    Token varName = consume(TokenType.IDENTIFIER, "Expected loop variable name.");
    consume(TokenType.EQUAL, "Expected '=' after loop variable.");
    Expr start = expression();

    consume(TokenType.TO, "Expected 'to' in for loop.");
    Expr end = expression();

    consume(TokenType.LEFT_BRACE, "Expected '{' after for header.");

    List<Stmt> body = new ArrayList<>();
    while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
        body.add(statement());
    }
    consume(TokenType.RIGHT_BRACE, "Expected '}' after for body.");

    // i = start
    Stmt init = new Stmt.Assignment(varName, start);

    // condition: i <= end
    Token lessEqual = new Token(
        TokenType.LESS_EQUAL,
        "<=",
        null,
        varName.line
    );

    Expr condition = new Expr.Binary(
        new Expr.Variable(varName),
        lessEqual,
        end
    );

    // i = i + 1
    Token plus = new Token(
        TokenType.PLUS,
        "+",
        null,
        varName.line
    );

    Expr incrementExpr = new Expr.Binary(
        new Expr.Variable(varName),
        plus,
        new Expr.Literal(1.0)
    );

    Stmt increment = new Stmt.Assignment(varName, incrementExpr);

    body.add(increment);

    Stmt whileLoop = new Stmt.While(condition, body);

    List<Stmt> block = new ArrayList<>();
    block.add(init);
    block.add(whileLoop);

    return new Stmt.Block(block);
}

    private Expr arrayLiteral() {
    List<Expr> elements = new ArrayList<>();

    if (!check(TokenType.RIGHT_BRACKET)) {
        do {
            elements.add(expression());
        } while (match(TokenType.COMMA));
    }

    consume(TokenType.RIGHT_BRACKET, "Expected ']' after array elements.");
    return new Expr.Array(elements);
}

    private Expr mapLiteral() {
        List<Expr> keys = new ArrayList<>();
        List<Expr> values = new ArrayList<>();

        if (!check(TokenType.RIGHT_BRACE)) {
            do {
                Expr key = expression();
                consume(TokenType.COLON, "Expected ':' after key.");
                Expr value = expression();
                keys.add(key);
                values.add(value);
            } while (match(TokenType.COMMA));
        }

        consume(TokenType.RIGHT_BRACE, "Expected '}' after map elements.");
        return new Expr.Map(keys, values);
    }

    private Stmt assignmentStatement() {
        Token name = consume(TokenType.IDENTIFIER, "Expected variable name.");
        consume(TokenType.EQUAL, "Expected '=' after variable name.");
        Expr value = expression();
        return new Stmt.Assignment(name, value);
    }

    private Expr expression() {
        return or();
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(TokenType.GREATER,TokenType.GREATER_EQUAL,TokenType.LESS,TokenType.LESS_EQUAL,TokenType.EQUAL_EQUAL,TokenType.BANG_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
    Expr expr = call();

    while (match(TokenType.STAR, TokenType.SLASH)) {
        Token operator = previous();
        Expr right = call();
        expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
}

    private Expr or() {
    Expr expr = and();

    while (match(TokenType.OR)) {
        Token operator = previous();
        Expr right = and();
        expr = new Expr.Logical(expr, operator, right);
    }

    return expr;
}

private Expr and() {
    Expr expr = comparison();

    while (match(TokenType.AND)) {
        Token operator = previous();
        Expr right = comparison();
        expr = new Expr.Logical(expr, operator, right);
    }

    return expr;
}



    private Stmt whileStatement() {
    Expr condition = expression();
    consume(TokenType.LEFT_BRACE, "Expected '{' after while condition.");

    List<Stmt> body = new ArrayList<>();
    while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
        body.add(statement());
    }

    consume(TokenType.RIGHT_BRACE, "Expected '}' after while body.");
    return new Stmt.While(condition, body);
}


    private Expr primary() {

    // 1️⃣ Parenthesized expression
    if (match(TokenType.LEFT_PAREN)) {
        Expr expr = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after expression.");
        return expr;
    }
    if (match(TokenType.FUN)) {
    consume(TokenType.LEFT_PAREN, "Expected '(' after 'fun'.");

    List<Token> params = new ArrayList<>();
    if (!check(TokenType.RIGHT_PAREN)) {
        do {
            params.add(
                consume(TokenType.IDENTIFIER, "Expected parameter name.")
            );
        } while (match(TokenType.COMMA));
    }

    consume(TokenType.RIGHT_PAREN, "Expected ')' after parameters.");
    consume(TokenType.LEFT_BRACE, "Expected '{' before lambda body.");

    List<Stmt> body = new ArrayList<>();
    while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
        body.add(statement());
    }

    consume(TokenType.RIGHT_BRACE, "Expected '}' after lambda body.");
    return new Expr.Lambda(params, body);
}


    // 2️⃣ Unary NOT
    if (match(TokenType.NOT)) {
        Token operator = previous();
        Expr right = primary();
        return new Expr.Logical(null, operator, right);
    }

    // 3️⃣ Literals
    if (match(TokenType.NUMBER)) {
        return new Expr.Literal(previous().literal);
    }

    if (match(TokenType.STRING)) {
        return new Expr.Literal(previous().literal);
    }

    if (match(TokenType.TRUE)) {
        return new Expr.Literal(true);
    }

    if (match(TokenType.FALSE)) {
        return new Expr.Literal(false);
    }
    // 4️⃣ Arrays
    if (match(TokenType.LEFT_BRACKET)) {
        return arrayLiteral();
    }

    // 5️⃣ Maps
    if (match(TokenType.LEFT_BRACE)) {
        return mapLiteral();
    }

    if (match(TokenType.THIS)) {
    return new Expr.This(previous());
}

    // 5️⃣ Variables + indexing + calls
    if (match(TokenType.IDENTIFIER)) {
        Expr expr = new Expr.Variable(previous());

        if (match(TokenType.LEFT_BRACKET)) {
            Expr index = expression();
            consume(TokenType.RIGHT_BRACKET, "Expected ']' after index.");
            return new Expr.Index(expr, index);
        }

        return expr;
    }

    throw error(peek(), "Expected expression.");
}



    // ---------- Utility Methods ----------

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean checkNext(TokenType type) {
    if (current + 1 >= tokens.size()) return false;
    return tokens.get(current + 1).type == type;
}

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private RuntimeException error(Token token, String message) {
    return new RuntimeException(
        "[line " + token.line + "] Syntax Error: " + message +
        (token.type == lexer.TokenType.EOF ? " at end of file." : " at '" + token.lexeme + "'.")
    );
}

}
