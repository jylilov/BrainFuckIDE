package by.jylilov.brainfuck;

import java.util.ArrayList;
import java.util.List;

public class BrainFuckProgram {
    private final List<BrainFuckOperation> list = new ArrayList<BrainFuckOperation>();

    public BrainFuckProgram(String sourceCode) {
        for (int i = 0; i < sourceCode.length(); ++i) {
            for (BrainFuckOperation operation : BrainFuckOperation.values()) {
                if (operation.getCharacter() == sourceCode.charAt(i)) {
                    list.add(operation);
                    break;
                }
            }
        }
    }

    public BrainFuckOperation getOperation(int operationIndex) {
        return list.get(operationIndex);
    }

    public int getLength() {
        return list.size();
    }
}
