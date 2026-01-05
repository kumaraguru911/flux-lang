package runtime;

import java.util.List;

public class BuiltinFunction {

    private final String name;
    private final NativeFunction function;

    public BuiltinFunction(String name, NativeFunction function) {
        this.name = name;
        this.function = function;
    }

    public Object call(List<Object> arguments) {
        if (arguments.size() != function.arity()) {
            throw new RuntimeException(
                "[Flux Runtime Error]\nFunction '" + name +
                "' expected " + function.arity() +
                " arguments but got " + arguments.size()
            );
        }
        return function.call(arguments);
    }

    @Override
    public String toString() {
        return "<builtin function " + name + ">";
    }
}
