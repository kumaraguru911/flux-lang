package runtime;

import java.util.HashMap;
import java.util.Map;
import lexer.Token;

public class FluxInstance {

    private final FluxClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    public FluxInstance(FluxClass klass, Map<String, Object> initialFields) {
        this.klass = klass;
        fields.putAll(initialFields);
    }

    public Object get(Token name) {

        // 1️⃣ Field access
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

        // 2️⃣ Method access → BIND HERE ✅
        FluxFunction method = klass.findMethod(name.lexeme);
        if (method != null) {
            return method.bind(this); // 🔥 THIS WAS MISSING
        }

        throw new RuntimeException(
            "[Flux Runtime Error]\nUndefined property '" + name.lexeme + "'."
        );
    }

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    
    @Override
public String toString() {
    return "<instance of " + klass.getName() + ">";
}
}
