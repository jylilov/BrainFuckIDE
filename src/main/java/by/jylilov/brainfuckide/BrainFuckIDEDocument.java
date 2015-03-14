package by.jylilov.brainfuckide;

import javax.swing.text.*;
import java.io.File;

public class BrainFuckIDEDocument extends DefaultStyledDocument {

    private File file = null;

    public BrainFuckIDEDocument() {
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
}
