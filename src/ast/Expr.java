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
    public static class Get extends Expr {
    public final Expr object;
    public final Token name;

    public Get(Expr object, Token name) {
        this.object = object;
        this.name = name;
    }
}

public static class Set extends Expr {
    public final Expr object;
    public final Token name;
    public final Expr value;

    public Set(Expr object, Token name, Expr value) {
        this.object = object;
        this.name = name;
        this.value = value;
    }
}

public static class This extends Expr {
    public final Token keyword;

    public This(Token keyword) {
        this.keyword = keyword;
    }
}


    public static class Array extends Expr {
    public final List<Expr> elements;

    public Array(List<Expr> elements) {
        this.elements = elements;
    }
}

    public static class Map extends Expr {
        public final List<Expr> keys;
        public final List<Expr> values;

        public Map(List<Expr> keys, List<Expr> values) {
            this.keys = keys;
            this.values = values;
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

    public static class Lambda extends Expr {
    public final List<Token> params;
    public final List<Stmt> body;

    public Lambda(List<Token> params, List<Stmt> body) {
        this.params = params;
        this.body = body;
    }
}

    public static class Logical extends Expr {
    public final Expr left;
    public final Token operator;
    public final Expr right;

    public Logical(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
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

    public static class Call extends Expr {
    public final Expr callee;
    public final List<Expr> arguments;

    public Call(Expr callee, List<Expr> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }
}

}
