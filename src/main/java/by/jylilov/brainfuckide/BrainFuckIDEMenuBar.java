package by.jylilov.brainfuckide;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class BrainFuckIDEMenuBar extends JMenuBar {

    private static final String FILE_MENU_CAPTION = "File";
    private static final String RUN_MENU_CAPTION = "Run";
    private static final String BUILD_MENU_CAPTION = "Build";

    public BrainFuckIDEMenuBar(BrainFuckIDEActions actions) {
        initializeFileMenu(actions);
        initializeBuildMenu(actions);
        initializeRunMenu(actions);
    }

    private void initializeBuildMenu(BrainFuckIDEActions actions) {
        JMenu menu = new JMenu(BUILD_MENU_CAPTION);
        menu.setMnemonic(KeyEvent.VK_B);
        menu.add(new JMenuItem(actions.getCompileAction()));
        add(menu);
    }

    private void initializeRunMenu(BrainFuckIDEActions actions) {
        JMenu menu;
        menu = new JMenu(RUN_MENU_CAPTION);
        menu.setMnemonic(KeyEvent.VK_U);
        menu.add(new JMenuItem(actions.getRunAction()));
        menu.add(new JSeparator());
        menu.add(new JMenuItem(actions.getDebugAction()));
        menu.add(new JMenuItem(actions.getStepAction()));
        menu.add(new JSeparator());
        menu.add(new JMenuItem(actions.getStopAction()));
        add(menu);
    }

    private void initializeFileMenu(BrainFuckIDEActions actions) {
        JMenu menu = new JMenu(FILE_MENU_CAPTION);
        menu.setMnemonic(KeyEvent.VK_F);
        menu.add(new JMenuItem(actions.getNewAction()));
        menu.add(new JMenuItem(actions.getOpenAction()));
        menu.add(new JSeparator());
        menu.add(new JMenuItem(actions.getSaveAction()));
        menu.add(new JMenuItem(actions.getSaveAsAction()));
        menu.add(new JMenuItem(actions.getCloseAction()));
        add(menu);
    }
}
