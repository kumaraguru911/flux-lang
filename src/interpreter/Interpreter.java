package interpreter;

import ast.Expr;
import ast.Stmt;
import java.util.ArrayList;
import java.util.List;
import runtime.Environment;
import runtime.ExitSignal;

public class Interpreter {

    private final Environment environment = new Environment();
    private boolean trace = false;

    public Interpreter() {}

    public Interpreter(boolean trace) {
        this.trace = trace;
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

        else if (stmt instanceof Stmt.Exit) {
            trace("Exit program");
            throw new ExitSignal();
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
                trace("While condition true");
                for (Stmt bodyStmt : whileStmt.body) {
                    execute(bodyStmt);
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
            return ((Expr.Literal) expr).value;
        }

        if (expr instanceof Expr.Variable) {
            return environment.get(((Expr.Variable) expr).name.lexeme);
        }

        if (expr instanceof Expr.Array) {
            List<Double> values = new ArrayList<>();
            for (Expr e : ((Expr.Array) expr).elements) {
                values.add((Double) evaluate(e));
            }
            return values;
        }

        if (expr instanceof Expr.Index) {
            Object arrayObj = evaluate(((Expr.Index) expr).array);
            Object indexObj = evaluate(((Expr.Index) expr).index);

            if (!(arrayObj instanceof List)) {
                throw runtimeError("Tried to index a non-array value.");
            }

            int idx = ((Double) indexObj).intValue();
            List<?> list = (List<?>) arrayObj;

            if (idx < 0 || idx >= list.size()) {
                throw new RuntimeException("Runtime Error: Array index out of bounds.");
            }

            return list.get(idx);
        }

        if (expr instanceof Expr.Binary) {
            Expr.Binary binary = (Expr.Binary) expr;
            Object left = evaluate(binary.left);
            Object right = evaluate(binary.right);

            Object result;

            switch (binary.operator.type) {
                case PLUS:
                    checkNumberOperands(left, right);
                    result = (double) left + (double) right;
                    break;
                case MINUS:
                    checkNumberOperands(left, right);
                    result = (double) left - (double) right;
                    break;
                case STAR:
                    checkNumberOperands(left, right);
                    result = (double) left * (double) right;
                    break;
                case SLASH:
                    checkNumberOperands(left, right);
                    result = (double) left / (double) right;
                    break;
                case GREATER:
                    result = (double) left > (double) right;
                    break;
                case GREATER_EQUAL:
                    result = (double) left >= (double) right;
                    break;
                case LESS:
                    result = (double) left < (double) right;
                    break;
                case LESS_EQUAL:
                    result = (double) left <= (double) right;
                    break;
                case EQUAL_EQUAL:
                    result = left.equals(right);
                    break;
                case BANG_EQUAL:
                    result = !left.equals(right);
                    break;
                default:
                    throw new RuntimeException("Runtime Error: Unknown operator.");
            }

            trace("Evaluate " + left + " " + binary.operator.lexeme + " " + right + " → " + result);
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
        return value == null ? "null" : value.toString();
    }

    private void checkNumberOperands(Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
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
