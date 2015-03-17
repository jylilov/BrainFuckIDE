package by.jylilov.brainfuckide;

import by.jylilov.brainfuck.BrainFuckInterpreter;
import by.jylilov.brainfuck.BrainFuckProgram;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;

public class BrainFuckIDEWindow extends JFrame {

    private static final String TITLE = "BrainFuck IDE";
    private static final String FILE_MENU_CAPTION = "File";
    private static final String RUN_MENU_CAPTION = "Run";
    private static final String NEW_ACTION_CAPTION = "New";
    private static final String OPEN_ACTION_CAPTION = "Open";
    private static final String SAVE_ACTION_CAPTION = "Save";
    private static final String SAVE_AS_ACTION_CAPTION = "Save as";
    private static final String CLOSE_ACTION_CAPTION = "Close";
    private static final String RUN_ACTION_CAPTION = "Run";
    private static final String DEFAULT_NEW_DOCUMENT_NAME = "noname.bf";

    private final BrainFuckIDEDocumentFilter documentFilter = new BrainFuckIDEDocumentFilter();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final Action newAction = new NewAction();
    private final Action openAction = new OpenAction();
    private final Action closeAction = new CloseAction();
    private final Action saveAction = new SaveAction();
    private final Action saveAsAction = new SaveAsAction();

    private final Action runAction = new RunAction();

    private final ChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener();

    public BrainFuckIDEWindow() {
        super(TITLE);
        setLayout(new BorderLayout());
        initializeTabbedPane();
        initializeMenuBar();
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(FILE_MENU_CAPTION);
        menu.setMnemonic(KeyEvent.VK_F);
        menu.add(new JMenuItem(newAction));
        menu.add(new JMenuItem(openAction));
        menu.add(new JSeparator());
        menu.add(new JMenuItem(saveAction));
        menu.add(new JMenuItem(saveAsAction));
        menu.add(new JMenuItem(closeAction));
        menuBar.add(menu);
        menu = new JMenu(RUN_MENU_CAPTION);
        menu.setMnemonic(KeyEvent.VK_U);
        menu.add(new JMenuItem(runAction));
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void initializeTabbedPane() {
        add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        tabbedPaneChangeListener.stateChanged(null);
    }

    private File chooseFile(ChooseFileType chooseFileType) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue;
        switch (chooseFileType) {
            case TO_OPEN:
                returnValue = fileChooser.showOpenDialog(this);
                break;
            case TO_SAVE:
                returnValue = fileChooser.showSaveDialog(this);
                break;
            default:
                throw new IllegalStateException();
        }
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private BrainFuckIDEDocument getCurrentDocument() {
        BrainFuckIDEDocument document;
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent == null) {
            return null;
        } else {
            document = ((BrainFuckIDEDocumentView)selectedComponent).getDocument();
            return document;
        }
    }

    private void addTab(BrainFuckIDEDocument document) {
        document.setDocumentFilter(documentFilter);
        File documentFile = document.getFile();
        String title = (documentFile == null ? DEFAULT_NEW_DOCUMENT_NAME : documentFile.getName());
        tabbedPane.addTab(title, new BrainFuckIDEDocumentView(document));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    private class NewAction extends AbstractAction {

        public NewAction() {
            super(NEW_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BrainFuckIDEDocument document = new BrainFuckIDEDocument();
            addTab(document);
        }

    }

    private class CloseAction extends AbstractAction {
        public CloseAction() {
            super(CLOSE_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            tabbedPane.remove(tabbedPane.getSelectedIndex());
        }

    }

    private class SaveAction extends AbstractAction {

        public SaveAction() {
            super(SAVE_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BrainFuckIDEDocument document = getCurrentDocument();
            File file = document.getFile();
            if (file != null) {
                document.saveToFile();
            } else {
                saveAsAction.actionPerformed(null);
            }
        }

    }

    private class SaveAsAction extends AbstractAction {

        public SaveAsAction() {
            super(SAVE_AS_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BrainFuckIDEDocument document = getCurrentDocument();
            File file = chooseFile(ChooseFileType.TO_SAVE);
            if (file != null) {
                tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
                document.setFile(file);
                document.saveToFile();
            }
        }

    }

    private class OpenAction extends AbstractAction {

        public OpenAction() {
            super(OPEN_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = chooseFile(ChooseFileType.TO_SAVE);
            if (file != null) {
                BrainFuckIDEDocument document = new BrainFuckIDEDocument(file);
                addTab(document);
            }
        }

    }

    private class RunAction extends AbstractAction {
        public RunAction() {
            super(RUN_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BrainFuckInterpreter interpreter = new BrainFuckInterpreter(System.in, System.out);
            interpreter.setProgram(new BrainFuckProgram(getCurrentDocument().getSourceCode()));
            interpreter.run();
        }
    }

    private class TabbedPaneChangeListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent e) {
            newAction.setEnabled(true);
            openAction.setEnabled(true);
            boolean state = tabbedPane.getTabCount() != 0;
            runAction.setEnabled(state);
            saveAction.setEnabled(state);
            saveAsAction.setEnabled(state);
            closeAction.setEnabled(state);
        }
    }

    private enum ChooseFileType {
        TO_OPEN, TO_SAVE
    }

}
