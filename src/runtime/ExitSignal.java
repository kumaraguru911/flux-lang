package runtime;

public class ExitSignal extends RuntimeException {
    public ExitSignal() {
        super("Program exited.");
    }
}
