package by.jylilov.brainfuck;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Observable;
import java.util.Stack;

public class BrainFuckInterpreter extends Observable {
    public static final int MEMORY_SIZE = 300;

    private final char memory[] = new char[MEMORY_SIZE];
    private final BrainFuckProgram program;
    private int dataPointer = 0;

    private Stack<Integer> cycleStack = new Stack<>();
    private int currentOperationIndex = 0;

    private InputStream inputStream;
    private OutputStream outputStream;

    public BrainFuckInterpreter(String sourceCode) {
        program = new BrainFuckProgram(sourceCode);
    }

    public boolean isExecutionFinished() {
        return currentOperationIndex == program.getLength();
    }

    public void executeOperation() {
        //TODO exceptions
        program.getOperation(currentOperationIndex).execute(this);
        ++currentOperationIndex;
    }

    void cycleEnd() {
        //TODO exceptions
        currentOperationIndex = cycleStack.pop() - 1;
    }

    void cycleBegin() {
        if (getCurrentData() == 0) {
            skipCycle();
        } else {
            cycleStack.add(currentOperationIndex);
        }
    }

    void output() {
        //TODO exceptions
        try {
            outputStream.write(getCurrentData());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void input() {
        //TODO exceptions
        try {
            setMemoryValue(dataPointer, (char) inputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void decrementPointer() {
        --dataPointer;
        validatePointer();
    }

    void incrementPointer() {
        ++dataPointer;
        validatePointer();
    }

    void decrementData() {
        setMemoryValue(dataPointer, getCurrentData() - 1);
    }

    public void incrementData() {
        setMemoryValue(dataPointer, getCurrentData() + 1);
    }

    private char getCurrentData() {
        return memory[dataPointer];
    }

    private void validatePointer() {
        if (dataPointer == -1) {
            dataPointer = MEMORY_SIZE - 1;
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

    public void setMemoryValue(int index, int value) {
        setMemoryValue(index, (char)(value % 256));
    }

    public void setMemoryValue(int index, char value) {
        //TODO: exceptions
        setChanged();
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

    public void stop() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteObservers();
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
