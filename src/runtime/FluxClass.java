package runtime;

import ast.Stmt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lexer.Token;

public class FluxClass {
    private final String name;
    private final List<Token> fields;
    private final Map<String, FluxFunction> methods;

    public FluxClass(String name, List<Token> fields, List<Stmt.Function> methods) {
    this.name = name;
    this.fields = fields;
    this.methods = new HashMap<>();
    for (Stmt.Function method : methods) {
        this.methods.put(
            method.name.lexeme,
            new FluxFunction(
                method.name,
                method.params,
                method.body,
                null
            )
        );
    }
}
public FluxFunction findMethod(String name) {
    return methods.get(name);
}
public String getName() {
    return name;
}
    public FluxInstance instantiate() {
        Map<String, Object> values = new HashMap<>();
        for (Token field : fields) {
            values.put(field.lexeme, null);
        }
        return new FluxInstance(this, values);
    }

    @Override
    public String toString() {
        return "<class " + name + ">";
    }
}