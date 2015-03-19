package by.jylilov.brainfuck;

import java.io.InputStream;

public class BrainFuckInterpreterStart {
    public static final String RESOURCE_SOURCE_CODE_NAME = "sourcecode.txt";

    public static void main(String []args) {
        InputStream inputStream = BrainFuckInterpreterStart.class.getClassLoader().getResourceAsStream(RESOURCE_SOURCE_CODE_NAME);
        String sourceCode = BrainFuckUtils.readStringFromStream(inputStream);
        BrainFuckInterpreter interpreter = new BrainFuckInterpreter(sourceCode);
        interpreter.setInputStream(System.in);
        interpreter.setOutputStream(System.out);
        interpreter.run();
    }
}
