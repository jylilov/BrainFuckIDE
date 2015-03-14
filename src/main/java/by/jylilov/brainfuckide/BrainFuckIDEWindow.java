package by.jylilov.brainfuckide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;

public class BrainFuckIDEWindow extends JFrame {

    private static final String TITLE = "BrainFuck IDE";
    private static final String FILE_MENU_CAPTION = "File";
    private static final String NEW_ACTION_CAPTION = "New";
    public static final String OPEN_ACTION_CAPTION = "Open";
    private static final String SAVE_ACTION_CAPTION = "Save";
    private static final String SAVE_AS_ACTION_CAPTION = "Save as";
    private static final String CLOSE_ACTION_CAPTION = "Close";
    private static final String DEFAULT_NEW_DOCUMENT_NAME = "noname.bf";

    private final BrainFuckIDEDocumentFilter documentFilter = new BrainFuckIDEDocumentFilter();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final Action newAction = new NewAction();
    private final Action openAction = new OpenAction();
    private final Action closeAction = new CloseAction();
    private final Action saveAction = new SaveAction();
    private final Action saveAsAction = new SaveAsAction();

    public BrainFuckIDEWindow() {
        super(TITLE);
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
    }

    private JMenuBar createMenuBar() {
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
        return menuBar;
    }

    private String getSourceCodeFromFile(File file) {
        String text = "";
        try {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private void saveSourceCodeToFile(String code, File file) {
        try {
            Writer writer = new FileWriter(file);
            writer.append(code);
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private File openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(BrainFuckIDEWindow.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private File createNewFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(BrainFuckIDEWindow.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }
    private BrainFuckIDEDocument getCurrentDocument() {
        BrainFuckIDEDocument document;
        document = ((BrainFuckIDEDocumentView)tabbedPane.getSelectedComponent()).getDocument();
        return document;
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

    private void addTab(BrainFuckIDEDocument document) {
        document.setDocumentFilter(documentFilter);
        File documentFile = document.getFile();
        String title = (documentFile == null ? DEFAULT_NEW_DOCUMENT_NAME : documentFile.getName());
        tabbedPane.addTab(title, new BrainFuckIDEDocumentView(document));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    private class CloseAction extends AbstractAction {
        public CloseAction() {
            super(CLOSE_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = tabbedPane.getSelectedIndex();
            if (index >= 0) {
                tabbedPane.remove(index);
            }
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
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex < 0) {
                return;
            }
            BrainFuckIDEDocument document = getCurrentDocument();
            File file = document.getFile();
            if (file == null) {
                file = createNewFile();
                if (file == null) {
                    return;
                }
                tabbedPane.setTitleAt(selectedIndex, file.getName());
                document.setFile(file);
            }
            String code = document.getSourceCode();
            saveSourceCodeToFile(code, file);
        }

    }

    private class SaveAsAction extends AbstractAction {

        public SaveAsAction() {
            super(SAVE_AS_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex < 0) {
                return;
            }
            BrainFuckIDEDocument document = getCurrentDocument();
            File file = createNewFile();
            if (file == null) {
                return;
            }
            tabbedPane.setTitleAt(selectedIndex, file.getName());
            document.setFile(file);
            String code = document.getSourceCode();
            saveSourceCodeToFile(code, file);
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
            BrainFuckIDEDocument document = new BrainFuckIDEDocument();
            File file = openFile();
            if (file == null) {
                return;
            }
            String sourceCode = getSourceCodeFromFile(file);
            document.setFile(file);
            addTab(document);
            document.setSourceCode(sourceCode);
        }

    }

}
