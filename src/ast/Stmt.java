package ast;

import lexer.Token;
import lexer.TokenType;

import java.util.List;

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

    public static class Assignment extends Stmt {
        public final Token name;
        public final Expr value;

        public Assignment(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }
    }

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


}
