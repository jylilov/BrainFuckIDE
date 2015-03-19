package by.jylilov.brainfuck;

import java.io.*;

public class BrainFuckUtils {
    private BrainFuckUtils() {}

    public static String getSourceCodeFromFile(File file) {
        String sourceCode = "";
        if (file == null) {
            return sourceCode;
        }
        try {
            Reader reader = new FileReader(file);
            sourceCode = getString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sourceCode;
    }

    public static String readStringFromStream(InputStream inputStream) {
        String text;
        InputStreamReader reader = new InputStreamReader(inputStream);
        try {
            text = getString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    private static String getString(Reader reader) throws IOException {
        String text = "";
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            text += line + "\n";
        }
        return text;
    }

    public static void writeInputToOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            while (true) {
                int readValue = inputStream.read();
                if (readValue < 0)
                    break;
                outputStream.write(readValue);
            }
        } finally {
            inputStream.close();
        }
    }
}
