package by.jylilov.brainfuckide;

import javax.swing.*;
import java.awt.*;

public class BrainFuckIDEDocumentView extends JComponent {

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
}
