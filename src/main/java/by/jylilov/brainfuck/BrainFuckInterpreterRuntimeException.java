package by.jylilov.brainfuck;

public class BrainFuckInterpreterRuntimeException extends RuntimeException {

    public BrainFuckInterpreterRuntimeException(String message) {
        super(message);
    }

    public BrainFuckInterpreterRuntimeException(Throwable cause) {
        super(cause);
    }
}
