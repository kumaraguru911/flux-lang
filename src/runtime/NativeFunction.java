package runtime;

import java.util.List;

public interface NativeFunction {
    Object call(List<Object> arguments);
    int arity();
}
