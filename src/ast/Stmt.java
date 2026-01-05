package ast;

import java.util.List;
import lexer.Token;

public abstract class Stmt {

    public static class Print extends Stmt {
    public final List<Expr> expressions;

    public Print(List<Expr> expressions) {
        this.expressions = expressions;
    }
}

    public static class Exit extends Stmt {
    public Exit() {}
}
    public static class Block extends Stmt {
    public final List<Stmt> statements;

    public Block(List<Stmt> statements) {
        this.statements = statements;
    }
}
    public static class Expression extends Stmt {
    public final Expr expression;

    public Expression(Expr expression) {
        this.expression = expression;
    }
}
    public static class Return extends Stmt {
    public final Expr value; // can be null

    public Return(Expr value) {
        this.value = value;
    }
}

    public static class Class extends Stmt {
    public final Token name;
    public final List<Token> fields;
    public final List<Stmt.Function> methods;

    public Class(Token name, List<Token> fields, List<Stmt.Function> methods) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }
}


    public static class Assignment extends Stmt {
        public final Token name;
        public final Expr value;

        public Assignment(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }
    }

    public static class Break extends Stmt {}

    public static class Continue extends Stmt {}


    public static class If extends Stmt {
        public final Expr condition;
        public final List<Stmt> thenBody;
        public final List<Stmt> elseBody; // NEW (nullable)

        public If(Expr condition, List<Stmt> thenBody, List<Stmt> elseBody) {
            this.condition = condition;
            this.thenBody = thenBody;
            this.elseBody = elseBody;
        }
    }

    public static class While extends Stmt {
    public final Expr condition;
    public final List<Stmt> body;

    public While(Expr condition, List<Stmt> body) {
        this.condition = condition;
        this.body = body;
    }
}

    public static class Function extends Stmt {
    public final Token name;
    public final List<Token> params;
    public final List<Stmt> body;

    public Function(Token name, List<Token> params, List<Stmt> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }
}



}
