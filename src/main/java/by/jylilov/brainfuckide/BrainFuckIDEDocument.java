package by.jylilov.brainfuckide;

import javax.swing.text.*;
import java.awt.*;
import java.io.*;

public class BrainFuckIDEDocument extends DefaultStyledDocument {

    private static final String DEFAULT_DOCUMENT_NAME = "noname.bf";

    private final BrainFuckIDEDocumentFilter filter = new BrainFuckIDEDocumentFilter();
    private File file = null;

    private int highlightCharacterPosition = -1;

    public BrainFuckIDEDocument() {
        setDocumentFilter(filter);
    }

    public BrainFuckIDEDocument(File file) {
        this();
        this.file = file;
        updateSourceCodeFromFile();
    }

    public String getSourceCode() {
        String answer = null;
        try {
            answer =  getText(0, getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public File getFile() {
        return file;
    }

    public void updateSourceCodeFromFile() {
        if (file == null) {
            return;
        }
        String sourceCode = "";
        try {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sourceCode += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSourceCode(sourceCode);
    }

    public String getName() {
        return file != null ? file.getName() : DEFAULT_DOCUMENT_NAME;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setSourceCode(String sourceCode) {
        try {
            this.remove(0, getLength());
            this.insertString(0, sourceCode, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        if (file == null) {
            return;
        }
        try {
            Writer writer = new FileWriter(file);
            writer.append(getSourceCode());
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void clearHighlight() {
        if (highlightCharacterPosition != -1) {
            try {
                replace(highlightCharacterPosition, 1, getText(highlightCharacterPosition, 1), null);
                highlightCharacterPosition = -1;
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void highlightCharacter(int characterPosition) {
        clearHighlight();
        highlightCharacterPosition = characterPosition;
        MutableAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setBackground(set, Color.RED);
        setCharacterAttributes(characterPosition, 1, set, false);
    }

}
