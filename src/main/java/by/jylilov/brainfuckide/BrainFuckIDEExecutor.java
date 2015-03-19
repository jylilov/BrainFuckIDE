package by.jylilov.brainfuckide;

import by.jylilov.brainfuck.BrainFuckInterpreter;
import by.jylilov.brainfuck.BrainFuckInterpreterRuntimeException;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BrainFuckIDEExecutor {
    private final Executor executor = Executors.newFixedThreadPool(2);

    private final BrainFuckInterpreter interpreter;
    private final BrainFuckIDEWindow window;

    private final PipedInputStream interfaceInputStream = new PipedInputStream();
    private final PipedOutputStream interfaceOutputStream = new PipedOutputStream();
    private final PipedInputStream interpreterInputStream = new PipedInputStream();
    private final PipedOutputStream interpreterOutputStream = new PipedOutputStream();

    private AtomicBoolean needStop = new AtomicBoolean(false);
    private AtomicBoolean needExecuteNext = new AtomicBoolean(false);

    public BrainFuckIDEExecutor(BrainFuckInterpreter interpreter, BrainFuckIDEWindow window) {
        this.interpreter = interpreter;
        this.window = window;
        initializeStreams();
        initializeInterpreter();
    }

    private void initializeInterpreter() {
        interpreter.setInputStream(interpreterInputStream);
        interpreter.setOutputStream(interpreterOutputStream);
    }

    private void initializeStreams() {
        try {
            interpreterOutputStream.connect(interfaceInputStream);
            interpreterInputStream.connect(interfaceOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BrainFuckInterpreter getInterpreter() {
        return interpreter;
    }

    public void inputInInterpreter(char character) {
        try {
            interfaceOutputStream.write(character);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        executor.execute(new InterpreterWork());
        executor.execute(new InterpreterOutputListener());
    }

    public void nextOperation() {
        needExecuteNext.set(true);
    }

    public void setNeedStop(boolean needStop) {
        this.needStop.set(needStop);
    }

    class InterpreterWork implements Runnable {
        @Override
        public void run() {
            prepareWindow();
            try {
                while (!needStop.get()) {
                    process();
                }
            } catch (BrainFuckInterpreterRuntimeException e) {
                window.showErrorMessage(e.getMessage());
                stop();
            }
        }

        private void prepareWindow() {
            if (window.getIdeState() == BrainFuckIDEState.DEBUG) {
                window.highlightOperation(interpreter.getCurrentOperationIndex());
            }
        }

        private void process() {
            if (interpreter.isExecutionFinished())
                stop();
            else {
                if (window.getIdeState() == BrainFuckIDEState.DEBUG) {
                    executeOperationInDebugMode();
                } else {
                    executeOperation();
                }
            }
        }

        private void stop() {
            window.getActions().getStopAction().actionPerformed();
            interpreter.stop();
        }

        private void executeOperationInDebugMode() {
            if (needExecuteNext.get()) {
                interpreter.executeOperation();
                window.highlightOperation(interpreter.getCurrentOperationIndex());
                needExecuteNext.set(false);
            }
        }

        private void executeOperation() {
            interpreter.executeOperation();
        }
    }

    class InterpreterOutputListener implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    int readValue = interfaceInputStream.read();
                    if (readValue == -1) {
                        break;
                    }
                    window.output((char)readValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
