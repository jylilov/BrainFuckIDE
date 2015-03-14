package by.jylilov.brainfuckide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class BrainFuckIDEWindow extends JFrame {

    private static final String TITLE = "BrainFuck IDE";
    private static final String FILE_MENU_CAPTION = "File";
    private static final String NEW_ACTION_CAPTION = "New";
    private static final String CLOSE_ACTION_CAPTION = "Close";
    private static final String DEFAULT_NEW_DOCUMENT_NAME = "noname.bf";

    private final BrainFuckIDEDocumentFilter documentFilter = new BrainFuckIDEDocumentFilter();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final Action newAction = new NewAction();
    private final Action closeAction = new CloseAction();

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
        JMenuItem newMenuItem = new JMenuItem(newAction);
        JMenuItem closeMenuItem = new JMenuItem(closeAction);
        menu.add(newMenuItem);
        menu.add(closeMenuItem);
        menuBar.add(menu);
        return menuBar;
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
            document.setDocumentFilter(documentFilter);
            tabbedPane.addTab(DEFAULT_NEW_DOCUMENT_NAME, new BrainFuckIDEDocumentView(document));
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
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
            int index = tabbedPane.getSelectedIndex();
            if (index >= 0) {
                tabbedPane.remove(index);
            }
        }
    }

}
