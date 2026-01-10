package runtime;

import java.util.ArrayList;
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

            case "upper":
                return new BuiltinFunction("upper", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        return new FluxString(value.toUpperCase());
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            case "lower":
                return new BuiltinFunction("lower", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        return new FluxString(value.toLowerCase());
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            case "split":
                return new BuiltinFunction("split", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        String delimiter = ((FluxString) arguments.get(0)).getValue();
                        String[] parts = value.split(java.util.regex.Pattern.quote(delimiter));
                        List<Object> result = new ArrayList<>();
                        for (String part : parts) {
                            result.add(new FluxString(part));
                        }
                        return new FluxArray(result);
                    }
                    @Override
                    public int arity() {
                        return 1;
                    }
                });

            case "trim":
                return new BuiltinFunction("trim", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        return new FluxString(value.trim());
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            case "startsWith":
                return new BuiltinFunction("startsWith", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        String prefix = ((FluxString) arguments.get(0)).getValue();
                        return value.startsWith(prefix);
                    }
                    @Override
                    public int arity() {
                        return 1;
                    }
                });

            case "endsWith":
                return new BuiltinFunction("endsWith", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        String suffix = ((FluxString) arguments.get(0)).getValue();
                        return value.endsWith(suffix);
                    }
                    @Override
                    public int arity() {
                        return 1;
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