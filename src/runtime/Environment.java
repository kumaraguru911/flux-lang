package runtime;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Object> values = new HashMap<>();
    private final Environment enclosing;

    // Global environment
    public Environment() {
        this.enclosing = null;
    }

    // Nested environment (for functions, blocks, etc.)
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }

        if (enclosing != null) {
            return enclosing.get(name);
        }

        throw new RuntimeException(
            "[Flux Runtime Error]\nUndefined variable '" + name + "'."
        );
    }

    public Map<String, Object> dump() {
        return values;
    }
}
