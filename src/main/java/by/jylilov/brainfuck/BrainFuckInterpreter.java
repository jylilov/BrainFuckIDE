package by.jylilov.brainfuck;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Stack;

public class BrainFuckInterpreter {
    private static final int MEMORY_SIZE = 30000;

    private BrainFuckProgram program;
    private char memory[] = new char[MEMORY_SIZE];
    private int dataPointer = 0;

    private Stack<Integer> cycleStack = new Stack<Integer>(); //TODO more detailed name;
    private int currentOperationIndex = 0;

    private InputStream inputStream;
    private OutputStream outputStream;

    public BrainFuckInterpreter(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void run() throws IOException {
        while (!isFinished()) {
            runOperation();
        }
    }

    private boolean isFinished() {
        return currentOperationIndex == program.getLength();
    }

    public void runOperation() throws IOException {
        //TODO if is finished throw exception
        switch (program.getOperation(currentOperationIndex)) {
            case INCREMENT_DATA:
                ++memory[dataPointer];
                break;
            case DECREMENT_DATA:
                --memory[dataPointer];
                break;
            case INCREMENT_POINTER:
                ++dataPointer;
                validatePointer();
                break;
            case DECREMENT_POINTER:
                --dataPointer;
                validatePointer();
                break;
            case INPUT:
                memory[dataPointer] = (char) inputStream.read();
                break;
            case OUTPUT:
                outputStream.write(memory[dataPointer]);
                outputStream.flush();
                break;
            case CYCLE_BEGIN:
                if (memory[dataPointer] == 0) {
                    skipCycle();
                    return;
                } else {
                    cycleStack.add(currentOperationIndex);
                }
                break;
            case CYCLE_END:
                //TODO run exceptions
                currentOperationIndex = cycleStack.pop();
                return;
            default:
                throw new IllegalStateException();
        }
        ++currentOperationIndex;
    }

    private void validatePointer() {
        switch (dataPointer) {
            case -1:
                dataPointer = MEMORY_SIZE;
                break;
            case MEMORY_SIZE:
                dataPointer = 0;
                break;
        }
    }

    private void skipCycle() {
        int cycleBeginCount = 1;
        ++currentOperationIndex;
        while (cycleBeginCount != 0) {
            switch (program.getOperation(currentOperationIndex)) {
                case CYCLE_BEGIN:
                    ++cycleBeginCount;
                    break;
                case CYCLE_END:
                    --cycleBeginCount;
                    break;
            }
            ++currentOperationIndex;
        }
    }

    public void resetState() {
        dataPointer = 0;
        currentOperationIndex = 0;
        cycleStack.removeAllElements();
        Arrays.fill(memory, (char)0);
    }

    public BrainFuckProgram getProgram() {
        return program;
    }

    public void setProgram(BrainFuckProgram program) {
        resetState();
        this.program = program;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
