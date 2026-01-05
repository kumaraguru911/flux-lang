package ast;

import java.util.List;

public class AstPrinter {

    public void print(List<Stmt> statements) {
        System.out.println("Program");
        for (Stmt stmt : statements) {
            printStmt(stmt, "├─ ");
        }
    }

    private void printStmt(Stmt stmt, String indent) {
    if (stmt instanceof Stmt.Print) {
        System.out.println(indent + "Print");
        for (Expr e : ((Stmt.Print) stmt).expressions) {
            printExpr(e, indent + "│  ");
        }
    }
    else if (stmt instanceof Stmt.Exit) {
        System.out.println(indent + "Exit");
    }
    else if (stmt instanceof Stmt.Function) {
    Stmt.Function fn = (Stmt.Function) stmt;

    System.out.println(indent + "Function " + fn.name.lexeme);

    if (!fn.params.isEmpty()) {
        System.out.print(indent + "├─ Params: ");
        for (int i = 0; i < fn.params.size(); i++) {
            System.out.print(fn.params.get(i).lexeme);
            if (i < fn.params.size() - 1) System.out.print(", ");
        }
        System.out.println();
    }

    System.out.println(indent + "└─ Body");
    for (Stmt s : fn.body) {
        printStmt(s, indent + "   ");
    }
}
    else if (stmt instanceof Stmt.Block) {
        System.out.println(indent + "Block");
        for (Stmt s : ((Stmt.Block) stmt).statements) {
            printStmt(s, indent + "│  ");
        }
    }
    else if (stmt instanceof Stmt.Assignment) {
        Stmt.Assignment a = (Stmt.Assignment) stmt;
        System.out.println(indent + "Assignment " + a.name.lexeme);
        printExpr(a.value, indent + "│  ");
    }
    else if (stmt instanceof Stmt.Return) {
    System.out.println(indent + "Return");
    Stmt.Return r = (Stmt.Return) stmt;
    if (r.value != null) {
        printExpr(r.value, indent + "│  ");
    }
}
    else if (stmt instanceof Stmt.Expression) {
    System.out.println(indent + "Expression");
    printExpr(((Stmt.Expression) stmt).expression, indent + "│  ");
}
    else if (stmt instanceof Stmt.If) {
        Stmt.If i = (Stmt.If) stmt;
        System.out.println(indent + "If");

        System.out.println(indent + "├─ Condition");
        printExpr(i.condition, indent + "│  ");

        System.out.println(indent + "├─ Then");
        for (Stmt s : i.thenBody) {
            printStmt(s, indent + "│  ");
        }

        if (i.elseBody != null) {
            System.out.println(indent + "└─ Else");
            for (Stmt s : i.elseBody) {
                printStmt(s, indent + "   ");
            }
        }
    }
    else if (stmt instanceof Stmt.While) {
        Stmt.While w = (Stmt.While) stmt;
        System.out.println(indent + "While");

        System.out.println(indent + "├─ Condition");
        printExpr(w.condition, indent + "│  ");

        System.out.println(indent + "└─ Body");
        for (Stmt s : w.body) {
            printStmt(s, indent + "   ");
        }
    }
}

    private void printExpr(Expr expr, String indent) {
    if (expr instanceof Expr.Literal) {
        System.out.println(indent + "Literal " + ((Expr.Literal) expr).value);
    }
    else if (expr instanceof Expr.Variable) {
        System.out.println(indent + "Variable " +
            ((Expr.Variable) expr).name.lexeme);
    }
    else if (expr instanceof Expr.Call) {
    Expr.Call call = (Expr.Call) expr;
    System.out.println(indent + "Call");

    System.out.println(indent + "├─ Callee");
    printExpr(call.callee, indent + "│  ");

    if (!call.arguments.isEmpty()) {
        System.out.println(indent + "└─ Arguments");
        for (Expr arg : call.arguments) {
            printExpr(arg, indent + "   ");
        }
    }
}
    else if (expr instanceof Expr.Get) {
    Expr.Get get = (Expr.Get) expr;
    System.out.println(indent + "Get");
    printExpr(get.object, indent + "├─ ");
    System.out.println(indent + "└─ " + get.name.lexeme);
}
    else if (expr instanceof Expr.Binary) {
        Expr.Binary b = (Expr.Binary) expr;
        System.out.println(indent + "Binary " + b.operator.lexeme);
        printExpr(b.left, indent + "├─ ");
        printExpr(b.right, indent + "└─ ");
    }
    else if (expr instanceof Expr.Array) {
        System.out.println(indent + "Array");
        for (Expr e : ((Expr.Array) expr).elements) {
            printExpr(e, indent + "│  ");
        }
    }
    else if (expr instanceof Expr.Index) {
        System.out.println(indent + "Index");
        printExpr(((Expr.Index) expr).array, indent + "├─ ");
        printExpr(((Expr.Index) expr).index, indent + "└─ ");
    }
}

}
