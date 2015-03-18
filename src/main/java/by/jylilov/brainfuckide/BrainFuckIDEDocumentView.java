package by.jylilov.brainfuckide;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class BrainFuckIDEDocumentView extends JComponent implements Observer {

    private final BrainFuckIDEDocument document;
    private final JTextPane editorPane = new JTextPane();
    private final JScrollPane scrollPane = new JScrollPane(editorPane);

    public BrainFuckIDEDocumentView(BrainFuckIDEDocument document) {
        this.document = document;
        setLayout(new BorderLayout());
        editorPane.setDocument(document);
        add(scrollPane);
    }

    public BrainFuckIDEDocument getDocument() {
        return document;
    }

    @Override
    public void update(Observable o, Object arg) {
        BrainFuckIDEState state = (BrainFuckIDEState)arg;
        editorPane.setEditable(state == BrainFuckIDEState.EDIT);
    }
}
