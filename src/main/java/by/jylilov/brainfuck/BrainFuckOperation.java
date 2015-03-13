package by.jylilov.brainfuck;

public enum BrainFuckOperation {
    INCREMENT_DATA('+'), DECREMENT_DATA('-'), INCREMENT_POINTER('>'), DECREMENT_POINTER('<'),
    OUTPUT('.'), INPUT(','), CYCLE_BEGIN('['), CYCLE_END(']');

    private char character;

    BrainFuckOperation(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }
}
