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

    private Stack<Integer> cycleStack = new Stack<Integer>();
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

    public void runOperation() {
        //TODO if is finished throw exception
        program.getOperation(currentOperationIndex).execute(this);
        ++currentOperationIndex;
    }

    public void cycleEnd() {
        //TODO run exceptions
        currentOperationIndex = cycleStack.pop();
        --currentOperationIndex;
    }

    public void cycleBegin() {
        if (memory[dataPointer] == 0) {
            skipCycle();
        } else {
            cycleStack.add(currentOperationIndex);
        }
    }

    public void output() {
        try {
            outputStream.write(memory[dataPointer]);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void input() {
        try {
            memory[dataPointer] = (char) inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decrementPointer() {
        --dataPointer;
        validatePointer();
    }

    public void incrementPointer() {
        ++dataPointer;
        validatePointer();
    }

    public void decrementData() {
        --memory[dataPointer];
    }

    public void incrementData() {
        ++memory[dataPointer];
    }

    private void validatePointer() {
        if (dataPointer == -1) {
            dataPointer = MEMORY_SIZE;
        } else if (dataPointer == MEMORY_SIZE) {
            dataPointer =0;
        }
    }

    private void skipCycle() {
        int cycleBeginCount = 1;
        ++currentOperationIndex;
        while (cycleBeginCount != 0) {
            BrainFuckOperation currentOperation = program.getOperation(currentOperationIndex);
            if (currentOperation == BrainFuckOperation.CYCLE_BEGIN) {
                ++cycleBeginCount;
            } else if (currentOperation == BrainFuckOperation.CYCLE_END) {
                --cycleBeginCount;
            }
            ++currentOperationIndex;
        }
        --currentOperationIndex;
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
