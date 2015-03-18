package by.jylilov.brainfuckide;

import by.jylilov.brainfuck.BrainFuckInterpreter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class BrainFuckIDEActions {
    private static final String NEW_ACTION_CAPTION = "New";
    private static final String OPEN_ACTION_CAPTION = "Open";
    private static final String SAVE_ACTION_CAPTION = "Save";
    private static final String SAVE_AS_ACTION_CAPTION = "Save as";
    private static final String CLOSE_ACTION_CAPTION = "Close";
    private static final String RUN_ACTION_CAPTION = "Run";
    private static final String STOP_ACTION_CAPTION = "Stop";

    private final BrainFuckIDEWindow window;

    private final AbstractBrainFuckIDEAction newAction;
    private final AbstractBrainFuckIDEAction saveAction;
    private final AbstractBrainFuckIDEAction saveAsAction;
    private final AbstractBrainFuckIDEAction closeAction;
    private final AbstractBrainFuckIDEAction openAction;
    private final AbstractBrainFuckIDEAction runAction;
    private final AbstractBrainFuckIDEAction stopAction;

    public BrainFuckIDEActions(BrainFuckIDEWindow window) {
        this.window = window;
        newAction = new NewAction();
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        closeAction = new CloseAction();
        openAction = new OpenAction();
        runAction = new RunAction();
        stopAction = new StopAction();
    }

    public AbstractBrainFuckIDEAction getNewAction() {
        return newAction;
    }

    public AbstractBrainFuckIDEAction getSaveAction() {
        return saveAction;
    }

    public AbstractBrainFuckIDEAction getSaveAsAction() {
        return saveAsAction;
    }

    public AbstractBrainFuckIDEAction getCloseAction() {
        return closeAction;
    }

    public AbstractBrainFuckIDEAction getOpenAction() {
        return openAction;
    }

    public AbstractBrainFuckIDEAction getRunAction() {
        return runAction;
    }

    public AbstractBrainFuckIDEAction getStopAction() {
        return stopAction;
    }

    public abstract class AbstractBrainFuckIDEAction extends AbstractAction implements Observer{
        public AbstractBrainFuckIDEAction() {
            window.addStateObserver(this);
            update(window.getIdeState());
        }

        public void actionPerformed() {
            if (isEnabled())
                actionPerformed(null);
        }

        @Override
        public void update(Observable o, Object arg) {
            update((BrainFuckIDEState)arg);
        }

        public abstract void update(BrainFuckIDEState ideState);
    }

    private class NewAction extends AbstractBrainFuckIDEAction {

        public NewAction() {
            putValue(NAME, NEW_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.NONE || ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            window.addTab();
        }

    }

    private class OpenAction extends AbstractBrainFuckIDEAction {

        public OpenAction() {
            putValue(NAME, OPEN_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.NONE || ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = window.chooseFileToOpen();
            if (file != null) {
                BrainFuckIDEDocument document = new BrainFuckIDEDocument(file);
                window.addTab(document);
            }
        }

    }

    private class SaveAction extends AbstractBrainFuckIDEAction {

        public SaveAction() {
            putValue(NAME, SAVE_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BrainFuckIDEDocument document = window.getActiveDocument();
            File file = document.getFile();
            if (file != null) {
                document.saveToFile();
            } else {
                saveAsAction.actionPerformed();
            }
        }

    }

    private class SaveAsAction extends AbstractBrainFuckIDEAction {

        public SaveAsAction() {
            putValue(NAME, SAVE_AS_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BrainFuckIDEDocument document = window.getActiveDocument();
            File file = window.chooseFileToSave();
            document.setFile(file);
            document.saveToFile();
            window.updateTabTitle();
        }

    }

    private class CloseAction extends AbstractBrainFuckIDEAction {

        public CloseAction() {
            putValue(NAME, CLOSE_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            window.removeActiveTab();
        }

    }

    private class RunAction extends AbstractBrainFuckIDEAction {
        public RunAction() {
            putValue(NAME, RUN_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String sourceCode = window.getActiveDocument().getSourceCode();
            BrainFuckInterpreter interpreter = new BrainFuckInterpreter(sourceCode);
            BrainFuckIDEExecutor executor = new BrainFuckIDEExecutor(interpreter, window);
            window.setExecutor(executor);
            window.setIdeState(BrainFuckIDEState.RUN);
            executor.execute();
        }
    }

    private class StopAction extends AbstractBrainFuckIDEAction {
        public StopAction() {
            putValue(NAME, STOP_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.RUN || ideState == BrainFuckIDEState.DEBUG);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BrainFuckIDEExecutor executor = window.getExecutor();
            executor.setNeedStop(true);
            window.setExecutor(null);
            window.setIdeState(BrainFuckIDEState.EDIT);
        }
    }
}
