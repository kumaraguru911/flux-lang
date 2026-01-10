package interpreter;

import ast.Expr;
import ast.Stmt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lexer.TokenType;
import runtime.BuiltinFunction;
import runtime.Environment;
import runtime.ExitSignal;
import runtime.FluxArray;
import runtime.FluxClass;
import runtime.FluxFunction;
import runtime.FluxInstance;
import runtime.FluxString;
import runtime.NativeFunction;
import runtime.ReturnSignal;

public class Interpreter {

    private Environment environment = new Environment();
    private boolean trace = false;

    public Interpreter() {
        defineBuiltins();
    }

    public Interpreter(boolean trace) {
        this.trace = trace;
        defineBuiltins();
    }

    public void interpret(List<Stmt> statements) {
        for (Stmt stmt : statements) {
            execute(stmt);
        }
    }

    // ===================== STATEMENTS =====================

    private void execute(Stmt stmt) {

        if (stmt instanceof Stmt.Print) {
            Stmt.Print printStmt = (Stmt.Print) stmt;

            StringBuilder output = new StringBuilder();
            for (Expr expr : printStmt.expressions) {
                Object value = evaluate(expr);
                output.append(stringify(value)).append(" ");
            }

            String result = output.toString().trim();
            trace("Print " + result);
            System.out.println(result);
        }
        
        else if (stmt instanceof Stmt.Class) {
    Stmt.Class cls = (Stmt.Class) stmt;
    FluxClass klass = new FluxClass(
        cls.name.lexeme,
        cls.fields,
        cls.methods
    );
    environment.define(cls.name.lexeme, klass);
}


        else if (stmt instanceof Stmt.Function) {
    Stmt.Function fn = (Stmt.Function) stmt;

    FluxFunction function = new FluxFunction(
        fn.name,
        fn.params,
        fn.body,
        environment
    );

    environment.define(fn.name.lexeme, function);

    trace("Define function " + fn.name.lexeme);
}

    else if (stmt instanceof Stmt.Expression) {
    evaluate(((Stmt.Expression) stmt).expression);
}
    else if (stmt instanceof Stmt.Break) {
    throw new runtime.BreakSignal();
}

else if (stmt instanceof Stmt.Continue) {
    throw new runtime.ContinueSignal();
}


    else if (stmt instanceof Stmt.Exit) {
            trace("Exit program");
            throw new ExitSignal();
        }
        else if (stmt instanceof Stmt.Return) {
    Stmt.Return r = (Stmt.Return) stmt;
    Object value = r.value == null ? null : evaluate(r.value);
    throw new ReturnSignal(value);
}


        else if (stmt instanceof Stmt.Block) {
            for (Stmt s : ((Stmt.Block) stmt).statements) {
                execute(s);
            }
        }

        else if (stmt instanceof Stmt.While) {
    Stmt.While whileStmt = (Stmt.While) stmt;
    trace("Entering while loop");

    while (isTruthy(evaluate(whileStmt.condition))) {
        try {
            for (Stmt bodyStmt : whileStmt.body) {
                execute(bodyStmt);
            }
        }
        catch (runtime.ContinueSignal c) {
            continue;
        }
        catch (runtime.BreakSignal b) {
            break;
        }
    }

    trace("Exiting while loop");
}


        else if (stmt instanceof Stmt.Assignment) {
            Stmt.Assignment assign = (Stmt.Assignment) stmt;
            Object value = evaluate(assign.value);
            environment.define(assign.name.lexeme, value);
            trace("Assign " + assign.name.lexeme + " = " + value);
        }

        else if (stmt instanceof Stmt.If) {
            Stmt.If ifStmt = (Stmt.If) stmt;
            Object condition = evaluate(ifStmt.condition);
            trace("Evaluate if condition → " + condition);

            if (isTruthy(condition)) {
                trace("Entering if block");
                for (Stmt bodyStmt : ifStmt.thenBody) {
                    execute(bodyStmt);
                }
            } else if (ifStmt.elseBody != null) {
                trace("Entering else block");
                for (Stmt bodyStmt : ifStmt.elseBody) {
                    execute(bodyStmt);
                }
            }
        }
    }

    // ===================== EXPRESSIONS =====================

    private Object evaluate(Expr expr) {

        if (expr instanceof Expr.Literal) {
            Object value = ((Expr.Literal) expr).value;
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return new FluxString((String) value);
            }
            return value;
        }

        if (expr instanceof Expr.Variable) {
            return environment.get(((Expr.Variable) expr).name.lexeme);
        }

        if (expr instanceof Expr.Array) {
            List<Object> values = new ArrayList<>();
            for (Expr e : ((Expr.Array) expr).elements) {
                values.add(evaluate(e));
            }
            return new FluxArray(values);
        }

        if (expr instanceof Expr.Map) {
            Map<Object, Object> map = new HashMap<>();
            List<Expr> keys = ((Expr.Map) expr).keys;
            List<Expr> values = ((Expr.Map) expr).values;
            for (int i = 0; i < keys.size(); i++) {
                Object key = evaluate(keys.get(i));
                if (key instanceof FluxString) {
                    key = ((FluxString) key).getValue();
                }
                Object value = evaluate(values.get(i));
                map.put(key, value);
            }
            return map;
        }

        if (expr instanceof Expr.Lambda) {
    Expr.Lambda lambda = (Expr.Lambda) expr;

    return new FluxFunction(
        null,                // no name
        lambda.params,
        lambda.body,
        environment          // closure captured here
    );
}
        if (expr instanceof Expr.This) {
    return environment.get("this");
}



        if (expr instanceof Expr.Index) {
            Object arrayObj = evaluate(((Expr.Index) expr).array);
            Object indexObj = evaluate(((Expr.Index) expr).index);

            if (arrayObj instanceof FluxArray) {
                int idx = ((Double) indexObj).intValue();
                List<?> list = ((FluxArray) arrayObj).getValue();
                if (idx < 0 || idx >= list.size()) {
                    throw new RuntimeException("Runtime Error: Array index out of bounds.");
                }
                return list.get(idx);
            } else if (arrayObj instanceof List) {
                int idx = ((Double) indexObj).intValue();
                List<?> list = (List<?>) arrayObj;
                if (idx < 0 || idx >= list.size()) {
                    throw new RuntimeException("Runtime Error: Array index out of bounds.");
                }
                return list.get(idx);
            } else if (arrayObj instanceof Map) {
                Object key = indexObj;
                if (key instanceof FluxString) {
                    key = ((FluxString) key).getValue();
                }
                return ((Map<?, ?>) arrayObj).get(key);
            } else {
                throw runtimeError("Tried to index a non-array, non-map value.");
            }
        }
        if (expr instanceof Expr.Logical) {
    Expr.Logical logical = (Expr.Logical) expr;

    if (logical.operator.type == TokenType.NOT) {
        Object right = evaluate(logical.right);
        return !isTruthy(right);
    }

    Object left = evaluate(logical.left);

    if (logical.operator.type == TokenType.OR) {
        if (isTruthy(left)) return true; // short-circuit
    }

    if (logical.operator.type == TokenType.AND) {
        if (!isTruthy(left)) return false; // short-circuit
    }

    Object right = evaluate(logical.right);
    return isTruthy(right);
}

    // ---------------- PROPERTY ACCESS ----------------
if (expr instanceof Expr.Get) {
    Expr.Get get = (Expr.Get) expr;
    Object object = evaluate(get.object);

    if (object instanceof FluxInstance) {
        Object value = ((FluxInstance) object).get(get.name);

        // ⭐⭐ THIS IS THE MISSING PIECE ⭐⭐
        if (value instanceof FluxFunction) {
            return ((FluxFunction) value).bind((FluxInstance) object);
        }

        return value;
    } else if (object instanceof FluxString) {
        return ((FluxString) object).get(get.name);
    } else if (object instanceof FluxArray) {
        return ((FluxArray) object).get(get.name);
    }

    throw runtimeError("Only instances, strings, and arrays have properties.");
}

// ---------------- PROPERTY SET ----------------
if (expr instanceof Expr.Set) {
    Expr.Set set = (Expr.Set) expr;
    Object object = evaluate(set.object);

    if (!(object instanceof runtime.FluxInstance)) {
        throw runtimeError("Only instances have fields.");
    }

    Object value = evaluate(set.value);
    ((runtime.FluxInstance) object).set(set.name, value);
    return value;
}


        if (expr instanceof Expr.Call) {
    Expr.Call call = (Expr.Call) expr;

    Object callee = evaluate(call.callee);

    // ---------- CLASS CONSTRUCTOR ----------
    if (callee instanceof FluxClass) {
        FluxInstance instance = ((FluxClass) callee).instantiate();

        // Call init if exists
        FluxFunction init = ((FluxClass) callee).findMethod("init");
        if (init != null) {
            FluxFunction boundInit = init.bind(instance);
            List<Object> args = new ArrayList<>(); // no args for init
            Environment localEnv = new Environment(boundInit.getClosure());
            Object result = null;
            Environment previous = this.environment;
            try {
                this.environment = localEnv;
                for (Stmt stmt : boundInit.getBody()) {
                    execute(stmt);
                }
            } catch (ReturnSignal r) {
                result = r.value;
            } finally {
                this.environment = previous;
            }
        }

        return instance;
    }

    // ---------- BUILTIN FUNCTION ----------
    if (callee instanceof BuiltinFunction) {
        BuiltinFunction fn = (BuiltinFunction) callee;
        List<Object> args = new ArrayList<>();
        for (Expr arg : call.arguments) {
            args.add(evaluate(arg));
        }
        return fn.call(args);
    }

    // ---------- USER FUNCTION / METHOD ----------
    if (!(callee instanceof FluxFunction)) {
        throw runtimeError("Can only call functions and classes.");
    }

    FluxFunction function = (FluxFunction) callee;

    if (call.arguments.size() != function.getParams().size()) {
        throw runtimeError(
            "Expected " + function.getParams().size() +
            " arguments but got " + call.arguments.size() + "."
        );
    }

    Environment localEnv = new Environment(function.getClosure());

    for (int i = 0; i < function.getParams().size(); i++) {
        String param = function.getParams().get(i).lexeme;
        Object value = evaluate(call.arguments.get(i));
        localEnv.define(param, value);
    }

    Object result = null;
    Environment previous = this.environment;

    try {
        this.environment = localEnv;
        for (Stmt stmt : function.getBody()) {
            execute(stmt);
        }
    }
    catch (ReturnSignal r) {
        result = r.value;
    }
    finally {
        this.environment = previous;
    }

    return result;
}


if (expr instanceof Expr.Get) {
    Expr.Get get = (Expr.Get) expr;
    Object object = evaluate(get.object);

    if (object instanceof FluxInstance) {
        Object value = ((FluxInstance) object).get(get.name);

        // ⭐⭐ THIS IS THE MISSING PIECE ⭐⭐
        if (value instanceof FluxFunction) {
            return ((FluxFunction) value).bind((FluxInstance) object);
        }

        return value;
    }

    throw runtimeError("Only instances have properties.");
}


if (expr instanceof Expr.Set) {
    Expr.Set set = (Expr.Set) expr;
    Object object = evaluate(set.object);

    if (!(object instanceof runtime.FluxInstance)) {
        throw runtimeError("Only instances have fields.");
    }

    Object value = evaluate(set.value);
    ((runtime.FluxInstance) object).set(set.name, value);
    return value;
}

if (expr instanceof Expr.Binary) {
    Expr.Binary binary = (Expr.Binary) expr;
    Object left = evaluate(binary.left);
    Object right = evaluate(binary.right);

    Object result;

    switch (binary.operator.type) {

    case PLUS:
        // Number addition OR string concatenation
        if ((left instanceof Double || left == null) && right instanceof Double) {
            double l = left == null ? 0.0 : (double) left;
            result = l + (double) right;
        } else {
            result = stringify(left) + stringify(right);
        }
        break;

    case MINUS:
        checkNumberOperands(left, right);
        double l = left == null ? 0.0 : (double) left;
        double r = right == null ? 0.0 : (double) right;
        result = l - r;
        break;

    case STAR:
        checkNumberOperands(left, right);
        l = left == null ? 0.0 : (double) left;
        r = right == null ? 0.0 : (double) right;
        result = l * r;
        break;

    case SLASH:
        checkNumberOperands(left, right);
        l = left == null ? 0.0 : (double) left;
        r = right == null ? 0.0 : (double) right;
        result = l / r;
        break;

    case PERCENT:
        checkNumberOperands(left, right);
        l = left == null ? 0.0 : (double) left;
        r = right == null ? 0.0 : (double) right;
        result = l % r;
        break;

    case GREATER:
        l = left == null ? 0.0 : (double) left;
        r = right == null ? 0.0 : (double) right;
        result = l > r;
        break;

    case GREATER_EQUAL:
        l = left == null ? 0.0 : (double) left;
        r = right == null ? 0.0 : (double) right;
        result = l >= r;
        break;

    case LESS:
        l = left == null ? 0.0 : (double) left;
        r = right == null ? 0.0 : (double) right;
        result = l < r;
        break;

    case LESS_EQUAL:
        l = left == null ? 0.0 : (double) left;
        r = right == null ? 0.0 : (double) right;
        result = l <= r;
        break;

    case EQUAL_EQUAL:
        if (left == null && right == null) {
            result = true;
        } else if (left == null || right == null) {
            result = false;
        } else {
            result = left.equals(right);
        }
        break;

    case BANG_EQUAL:
        if (left == null && right == null) {
            result = false;
        } else if (left == null || right == null) {
            result = true;
        } else {
            result = !left.equals(right);
        }
        break;

    default:
        throw new RuntimeException("Runtime Error: Unknown operator.");
}


    trace("Evaluate " + left + " " + binary.operator.lexeme + " → " + result);
    return result;
}

throw new RuntimeException("Runtime Error: Unknown expression.");

    }

    // ===================== HELPERS =====================

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (boolean) value;
        if (value instanceof Double) return (double) value != 0;
        return true;
    }

    private String stringify(Object value) {
    if (value == null) return "null";
    if (value instanceof FluxFunction) return "<function>";
    return value.toString();
}


    private void checkNumberOperands(Object left, Object right) {
        if ((left instanceof Double || left == null) && (right instanceof Double || right == null)) return;
        throw new RuntimeException("Runtime Error: Operands must be numbers.");
    }

    private void trace(String message) {
        if (trace) {
            System.out.println("[TRACE] " + message);
        }
    }
    private void executeBlock(List<Stmt> statements) {
    for (Stmt stmt : statements) {
        execute(stmt);
    }
}

    private void defineBuiltins() {

    // len(array)
    environment.define("len", new BuiltinFunction("len", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            Object value = arguments.get(0);
            if (value instanceof List) {
                return (double) ((List<?>) value).size();
            }
            throw runtimeError("len() expects an array.");
        }

        @Override
        public int arity() {
            return 1;
        }
    }));


    // type(value)
    environment.define("type", new BuiltinFunction("type", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            Object v = arguments.get(0);
            if (v instanceof Double) return "number";
            if (v instanceof FluxString) return "string";
            if (v instanceof Boolean) return "boolean";
            if (v instanceof List) return "array";
            if (v instanceof Map) return "map";
            if (v instanceof FluxFunction) return "function";
            return "unknown";
        }

        @Override
        public int arity() {
            return 1;
        }
    }));


    // range(start, end)
    environment.define("range", new BuiltinFunction("range", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double start = (double) arguments.get(0);
            double end = (double) arguments.get(1);

            List<Object> result = new ArrayList<>();
            for (int i = (int) start; i < (int) end; i++) {
                result.add((double) i);
            }
            return new FluxArray(result);
        }

        @Override
        public int arity() {
            return 2;
        }
    }));

    // floor(number)
    environment.define("floor", new BuiltinFunction("floor", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double value = (double) arguments.get(0);
            return (double) Math.floor(value);
        }

        @Override
        public int arity() {
            return 1;
        }
    }));

    // ceil(number)
    environment.define("ceil", new BuiltinFunction("ceil", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double value = (double) arguments.get(0);
            return (double) Math.ceil(value);
        }

        @Override
        public int arity() {
            return 1;
        }
    }));

    // round(number)
    environment.define("round", new BuiltinFunction("round", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double value = (double) arguments.get(0);
            return (double) Math.round(value);
        }

        @Override
        public int arity() {
            return 1;
        }
    }));

    // sqrt(number)
    environment.define("sqrt", new BuiltinFunction("sqrt", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double value = (double) arguments.get(0);
            return Math.sqrt(value);
        }

        @Override
        public int arity() {
            return 1;
        }
    }));

    // abs(number)
    environment.define("abs", new BuiltinFunction("abs", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double value = (double) arguments.get(0);
            return Math.abs(value);
        }

        @Override
        public int arity() {
            return 1;
        }
    }));

    // min(a, b)
    environment.define("min", new BuiltinFunction("min", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double a = (double) arguments.get(0);
            double b = (double) arguments.get(1);
            return Math.min(a, b);
        }

        @Override
        public int arity() {
            return 2;
        }
    }));

    // max(a, b)
    environment.define("max", new BuiltinFunction("max", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            double a = (double) arguments.get(0);
            double b = (double) arguments.get(1);
            return Math.max(a, b);
        }

        @Override
        public int arity() {
            return 2;
        }
    }));

    // toNumber(value)
    environment.define("toNumber", new BuiltinFunction("toNumber", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            Object value = arguments.get(0);
            if (value instanceof Double) return value;
            if (value instanceof Boolean) return ((Boolean) value) ? 1.0 : 0.0;
            if (value instanceof FluxString) {
                try {
                    return Double.parseDouble(((FluxString) value).getValue());
                } catch (NumberFormatException e) {
                    throw new RuntimeException("[Flux Runtime Error]\nCannot convert string to number.");
                }
            }
            throw new RuntimeException("[Flux Runtime Error]\nCannot convert value to number.");
        }

        @Override
        public int arity() {
            return 1;
        }
    }));

    // toString(value)
    environment.define("toString", new BuiltinFunction("toString", new NativeFunction() {
        @Override
        public Object call(List<Object> arguments) {
            Object value = arguments.get(0);
            if (value == null) return new FluxString("null");
            if (value instanceof FluxString) return value;
            if (value instanceof FluxArray) return new FluxString(value.toString());
            if (value instanceof Boolean) return new FluxString(value.toString());
            if (value instanceof Double) return new FluxString(String.format("%g", value));
            return new FluxString(value.toString());
        }

        @Override
        public int arity() {
            return 1;
        }
    }));
}

    public void dumpEnvironment() {
    System.out.println("Environment:");
    for (var entry : environment.dump().entrySet()) {
        System.out.println(entry.getKey() + " = " + entry.getValue());
    }
}

    private RuntimeException runtimeError(String message) {
    return new RuntimeException("[Flux Runtime Error]\n" + message);
}


}
