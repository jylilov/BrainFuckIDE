package by.jylilov.brainfuck;

import java.util.Stack;

public class BrainFuckInterpreter {
    private static final int MEMORY_SIZE = 30000;

    private BrainFuckProgram program;
    private char memory[] = new char[MEMORY_SIZE];
    private int dataPointer = 0;

    private Stack<Integer> stack = new Stack<Integer>(); //TODO more detailed name;
    private int currentOperationIndex = 0;

    public BrainFuckInterpreter() {}

    public void run() {
        while (currentOperationIndex < program.getLength()) {
            runOperation();
        }
    }

    public void runOperation() {
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
                //TODO Input stream
                break;
            case OUTPUT:
                //TODO Output stream
                break;
            case CYCLE_BEGIN:
                if (memory[currentOperationIndex] == 0) {
                    skipCycle();
                } else {
                    stack.add(currentOperationIndex);
                    ++currentOperationIndex;
                }
                break;
            case CYCLE_END:
                //TODO run exceptions
                dataPointer = stack.pop();
                break;
            default:
                throw new IllegalStateException();
        }
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

    public BrainFuckProgram getProgram() {
        return program;
    }

    public void setProgram(BrainFuckProgram program) {
        this.program = program;
    }
}
