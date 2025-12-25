package parser;

import ast.Expr;
import ast.Stmt;
import lexer.Token;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(statement());
        }
        return statements;
    }

    private Stmt statement() {
    if (match(TokenType.PRINT)) return printStatement();
    if (match(TokenType.EXIT)) return new Stmt.Exit();
    if (match(TokenType.IF)) return ifStatement();
    if (match(TokenType.WHILE)) return whileStatement();
    if (match(TokenType.FOR)) return forStatement();
    return assignmentStatement();
}


    private Stmt printStatement() {
    List<Expr> values = new ArrayList<>();
    values.add(expression());

    while (match(TokenType.COMMA)) {
        values.add(expression());
    }

    return new Stmt.Print(values);
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

    private Stmt assignmentStatement() {
        Token name = consume(TokenType.IDENTIFIER, "Expected variable name.");
        consume(TokenType.EQUAL, "Expected '=' after variable name.");
        Expr value = expression();
        return new Stmt.Assignment(name, value);
    }

    private Expr expression() {
        return comparison();
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
        Expr expr = primary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expr right = primary();
            expr = new Expr.Binary(expr, operator, right);
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
    if (match(TokenType.NUMBER)) {
        return new Expr.Literal(previous().literal);
    }

    if (match(TokenType.LEFT_BRACKET)) {
    return arrayLiteral();
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
