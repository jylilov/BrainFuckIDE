package by.jylilov.brainfuck;

public enum BrainFuckOperation implements BrainFuckOperationInterface {
    INCREMENT_DATA('+', new IncrementDataOperation()),
    DECREMENT_DATA('-', new DecrementDataOperation()),
    INCREMENT_POINTER('>', new IncrementPointerOperation()),
    DECREMENT_POINTER('<', new DecrementPointerOperation()),
    OUTPUT('.', new OutputOperation()),
    INPUT(',', new InputOperation()),
    CYCLE_BEGIN('[', new CycleBeginOperation()),
    CYCLE_END(']', new CycleEndOperation());

    private char character;
    private BrainFuckOperationInterface command;

    BrainFuckOperation(char character, BrainFuckOperationInterface command) {
        this.character = character;
        this.command = command;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public void execute(BrainFuckInterpreter interpreter) {
        command.execute(interpreter);
    }

    private static class IncrementDataOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.incrementData();
        }
    }

    private static class DecrementDataOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.decrementData();
        }
    }

    private static class IncrementPointerOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.incrementPointer();
        }
    }

    private static class DecrementPointerOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.decrementPointer();
        }
    }

    private static class OutputOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.output();
        }
    }

    private static class InputOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.input();
        }
    }

    private static class CycleBeginOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.cycleBegin();
        }
    }

    private static class CycleEndOperation implements BrainFuckOperationInterface {
        @Override
        public void execute(BrainFuckInterpreter interpreter) {
            interpreter.cycleEnd();
        }
    }
}
