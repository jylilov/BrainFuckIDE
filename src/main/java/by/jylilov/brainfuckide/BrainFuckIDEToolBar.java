package by.jylilov.brainfuckide;

import javax.swing.*;

public class BrainFuckIDEToolBar extends JToolBar {

    public final BrainFuckIDEActions actions;

    public BrainFuckIDEToolBar(BrainFuckIDEActions actions) {
        super(JToolBar.HORIZONTAL);
        this.actions = actions;
        add(new JButton(actions.getRunAction()));
        add(new JButton(actions.getDebugAction()));
        add(new JButton(actions.getStopAction()));
        add(new JButton(actions.getStepAction()));
    }
}
