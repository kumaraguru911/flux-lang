package ast;

import java.util.List;

import lexer.Token;

public abstract class Expr {

    public static class Binary extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;

        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }

    public static class Array extends Expr {
    public final List<Expr> elements;

    public Array(List<Expr> elements) {
        this.elements = elements;
    }
}

public static class Index extends Expr {
    public final Expr array;
    public final Expr index;

    public Index(Expr array, Expr index) {
        this.array = array;
        this.index = index;
    }
}

    public static class Literal extends Expr {
        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }
    }

    public static class Variable extends Expr {
        public final Token name;

        public Variable(Token name) {
            this.name = name;
        }
    }
}
