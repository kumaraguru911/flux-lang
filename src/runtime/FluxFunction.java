package runtime;

import ast.Stmt;
import java.util.List;
import lexer.Token;

public class FluxFunction {

    private final Token name;
    private final List<Token> params;
    private final List<Stmt> body;
    private final Environment closure;

    public FluxFunction(Token name, List<Token> params, List<Stmt> body, Environment closure) {
        this.name = name;
        this.params = params;
        this.body = body;
        this.closure = closure;
    }

    // 🔥 THIS IS THE CRITICAL METHOD
    public FluxFunction bind(FluxInstance instance) {
        Environment env = new Environment(closure);
        env.define("this", instance);
        return new FluxFunction(name, params, body, env);
    }

    public List<Token> getParams() {
        return params;
    }

    public List<Stmt> getBody() {
        return body;
    }

    public Environment getClosure() {
        return closure;
    }

    @Override
    public String toString() {
        return "<fn " + name.lexeme + ">";
    }
}
