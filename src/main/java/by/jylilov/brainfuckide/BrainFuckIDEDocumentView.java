package by.jylilov.brainfuckide;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class BrainFuckIDEDocumentView extends JComponent {

    private final BrainFuckIDEDocument model;
    private final JTextPane editorPane = new JTextPane();
    private final JScrollPane scrollPane = new JScrollPane(editorPane);

    public BrainFuckIDEDocumentView(BrainFuckIDEDocument model) {
        this.model = model;
        setLayout(new BorderLayout());
        editorPane.setDocument(model);
        add(scrollPane);
    }
}
