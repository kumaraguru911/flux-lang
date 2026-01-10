package runtime;

import java.util.List;
import lexer.Token;

public class FluxArray {
    private final List<Object> value;

    public FluxArray(List<Object> value) {
        this.value = value;
    }

    public Object get(Token name) {
        switch (name.lexeme) {
            case "len":
                return new BuiltinFunction("len", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        return (double) value.size();
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            case "push":
                return new BuiltinFunction("push", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        value.add(arguments.get(0));
                        return (double) value.size();
                    }
                    @Override
                    public int arity() {
                        return 1;
                    }
                });

            case "pop":
                return new BuiltinFunction("pop", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        if (value.isEmpty()) {
                            throw new RuntimeException("[Flux Runtime Error]\nCannot pop from empty array.");
                        }
                        return value.remove(value.size() - 1);
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            case "shift":
                return new BuiltinFunction("shift", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        if (value.isEmpty()) {
                            throw new RuntimeException("[Flux Runtime Error]\nCannot shift from empty array.");
                        }
                        return value.remove(0);
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            case "unshift":
                return new BuiltinFunction("unshift", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        value.add(0, arguments.get(0));
                        return (double) value.size();
                    }
                    @Override
                    public int arity() {
                        return 1;
                    }
                });

            case "contains":
                return new BuiltinFunction("contains", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        return value.contains(arguments.get(0));
                    }
                    @Override
                    public int arity() {
                        return 1;
                    }
                });

            case "indexOf":
                return new BuiltinFunction("indexOf", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        int idx = value.indexOf(arguments.get(0));
                        return idx >= 0 ? (double) idx : -1.0;
                    }
                    @Override
                    public int arity() {
                        return 1;
                    }
                });

            case "reverse":
                return new BuiltinFunction("reverse", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        java.util.Collections.reverse(value);
                        return new FluxArray(value);
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            case "sort":
                return new BuiltinFunction("sort", new NativeFunction() {
                    @Override
                    public Object call(List<Object> arguments) {
                        value.sort((a, b) -> {
                            if (a instanceof Double && b instanceof Double) {
                                return Double.compare((Double) a, (Double) b);
                            }
                            return 0;
                        });
                        return new FluxArray(value);
                    }
                    @Override
                    public int arity() {
                        return 0;
                    }
                });

            default:
                throw new RuntimeException("[Flux Runtime Error]\nUndefined method '" + name.lexeme + "' on array.");
        }
    }

    public List<Object> getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < value.size(); i++) {
            if (i > 0) sb.append(", ");
            Object obj = value.get(i);
            if (obj instanceof FluxString) {
                sb.append('"').append(((FluxString) obj).getValue()).append('"');
            } else {
                sb.append(obj);
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
