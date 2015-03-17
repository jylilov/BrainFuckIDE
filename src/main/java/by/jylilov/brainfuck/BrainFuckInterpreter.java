package by.jylilov.brainfuck;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Observable;
import java.util.Stack;

public class BrainFuckInterpreter extends Observable {
    public static final int MEMORY_SIZE = 300;

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

    public void run() {
        //TODO exceptions
        while (!isFinished()) {
            runOperation();
        }
    }

    private boolean isFinished() {
        return currentOperationIndex == program.getLength();
    }

    public void runOperation() {
        //TODO exceptions
        program.getOperation(currentOperationIndex).execute(this);
        ++currentOperationIndex;
    }

    public void cycleEnd() {
        //TODO exceptions
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
        //TODO exceptions
        try {
            outputStream.write(memory[dataPointer]);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void input() {
        //TODO exceptions
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

    public char getCurrentData() {
        return memory[dataPointer];
    }

    public void decrementData() {
        setMemoryValue(dataPointer, (char)(getCurrentData() - 1));
    }

    public void incrementData() {
        setMemoryValue(dataPointer, (char)(getCurrentData() + 1));
    }

    private void validatePointer() {
        if (dataPointer == -1) {
            dataPointer = MEMORY_SIZE;
        } else if (dataPointer == MEMORY_SIZE) {
            dataPointer =0;
        }
    }

    private void skipCycle() {
        int cycleBeginCount = 0;
        ++currentOperationIndex;
        while (true) {
            BrainFuckOperation currentOperation = program.getOperation(currentOperationIndex);
            if (currentOperation == BrainFuckOperation.CYCLE_BEGIN) {
                //TODO exceptions
                ++cycleBeginCount;
            } else if (currentOperation == BrainFuckOperation.CYCLE_END) {
                if (cycleBeginCount != 0) {
                    --cycleBeginCount;
                } else {
                    break;
                }
            }
            ++currentOperationIndex;
        }
    }

    public void setMemoryValue(int index, char value) {
        //TODO: exceptions
        notifyObservers();
        if (value > 255) {
            value = 255;
        }
        memory[index] = value;
    }

    public char getMemoryValue(int index) {
        //TODO: exceptions
        return memory[index];
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
