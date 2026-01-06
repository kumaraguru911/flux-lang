package runtime;

import java.util.List;
import lexer.Token;

public class FluxString {
    private final String value;

    public FluxString(String value) {
        this.value = value;
    }

    public Object get(Token name) {
        switch (name.lexeme) {
            case "len":
                return new BuiltinFunction("len", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        return (double) value.length();
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });
            case "substring":
                return new BuiltinFunction("substring", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        int start = ((Double) arguments.get(0)).intValue();
                        int end = ((Double) arguments.get(1)).intValue();
                        return new FluxString(value.substring(start, end));
                    }
                    @Override
                    public int arity() {
                        return 2;
                    }
                });
            default:
                throw new RuntimeException("[Flux Runtime Error]\nUndefined method '" + name.lexeme + "' on string.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}