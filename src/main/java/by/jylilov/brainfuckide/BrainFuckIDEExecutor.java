package by.jylilov.brainfuckide;

import by.jylilov.brainfuck.BrainFuckInterpreter;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BrainFuckIDEExecutor {
    private final BrainFuckInterpreter interpreter;
    private final BrainFuckIDEWindow window;

    private final PipedInputStream interfaceInputStream = new PipedInputStream();
    private final PipedOutputStream interfaceOutputStream = new PipedOutputStream();
    private final PipedInputStream interpreterInputStream = new PipedInputStream();
    private final PipedOutputStream interpreterOutputStream = new PipedOutputStream();

    private AtomicBoolean needStop = new AtomicBoolean(false);

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
        Executor executor = Executors.newFixedThreadPool(2);
        executor.execute(new InterpreterWork());
        executor.execute(new InterpreterOutputListener());
    }

    public boolean getNeedStop() {
        return needStop.get();
    }

    public void setNeedStop(boolean needStop) {
        this.needStop.set(needStop);
    }

    //TODO remove System.out log
    class InterpreterWork implements Runnable {
        @Override
        public void run() {
            System.out.println("Interpreter start");
            while (!needStop.get()) {
                if (interpreter.isExecutionFinished())
                    window.getActions().getStopAction().actionPerformed();
                else
                    interpreter.executeOperation();
            }
            interpreter.stop();
            System.out.println("Interpreter finished");
        }
    }

    class InterpreterOutputListener implements Runnable {
        @Override
        public void run() {
            System.out.println("OutputListener start");
            while (true) {
                try {
                    int readValue = interfaceInputStream.read();
                    if (readValue == -1) {
                        break;
                    }
                    window.output((char)readValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("OutputListener finished");
        }
    }
}
