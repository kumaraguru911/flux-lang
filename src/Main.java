import ast.AstPrinter;
import ast.Stmt;
import interpreter.Interpreter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

public class Main {

    public static void main(String[] args) throws Exception {

        // ===== REPL MODE =====
        if (args.length >= 1 && args[0].equals("repl")) {
            startRepl();
            return;
        }

        // ===== CLI MODE =====
        if (args.length < 2) {
            System.out.println("Flux CLI");
            System.out.println("Usage:");
            System.out.println("  .\\flux run <file.flux>");
            System.out.println("  .\\flux ast <file.flux>");
            System.out.println("  .\\flux trace <file.flux>");
            System.out.println("  .\\flux env <file.flux>");
            System.out.println("  .\\flux repl");
            return;
        }

        String command = args[0];
        String filePath = args[1];

        Path path = Path.of(filePath).toAbsolutePath();
        String source = Files.readString(path);

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        try {
            switch (command) {

                case "run" -> {
                    Interpreter interpreter = new Interpreter(false);
                    interpreter.interpret(statements);
                }

                case "trace" -> {
                    Interpreter interpreter = new Interpreter(true);
                    interpreter.interpret(statements);
                }

                case "ast" -> {
                    AstPrinter printer = new AstPrinter();
                    printer.print(statements);
                }

                case "env" -> {
                    Interpreter interpreter = new Interpreter(false);
                    try {
                        interpreter.interpret(statements);
                    } catch (runtime.ExitSignal e) {
                        // stop execution, continue
                    }
                    interpreter.dumpEnvironment();
                }

                default -> {
                    System.out.println("Unknown command: " + command);
                }
            }
        } catch (runtime.ExitSignal e) {
            // clean exit
        }
    }

    // ===== REPL IMPLEMENTATION =====
    private static void startRepl() {
        System.out.println("Flux REPL");
        System.out.println("Type 'exit' to quit.");

        Interpreter interpreter = new Interpreter(false);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                Lexer lexer = new Lexer(line);
                List<Token> tokens = lexer.scanTokens();
                Parser parser = new Parser(tokens);
                List<Stmt> statements = parser.parse();
                interpreter.interpret(statements);
            } catch (runtime.ExitSignal e) {
                System.out.println("Exiting REPL.");
                break;
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
