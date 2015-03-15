package by.jylilov.brainfuckide;

import javax.swing.text.*;
import java.io.*;

public class BrainFuckIDEDocument extends DefaultStyledDocument {

    private File file = null;

    public BrainFuckIDEDocument() {
    }

    public BrainFuckIDEDocument(File file) {
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

    @Override
    public void setDocumentFilter(DocumentFilter filter) {
        super.setDocumentFilter(filter);
        setSourceCode(getSourceCode());
    }
}
