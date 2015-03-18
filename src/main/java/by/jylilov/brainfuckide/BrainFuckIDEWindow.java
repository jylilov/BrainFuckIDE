package by.jylilov.brainfuckide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class BrainFuckIDEWindow extends JFrame {

    private static final String TITLE = "BrainFuck IDE";

    private static final String FILE_MENU_CAPTION = "File";
    private static final String RUN_MENU_CAPTION = "Run";
    public static final String BRAIN_FUCK_IDE_STATE_PROPERTY = "BrainFuckIDEState";

    private StateObservable stateObservable = new StateObservable();
    private BrainFuckIDEState ideState = BrainFuckIDEState.NONE;

    private final BrainFuckIDEActions actions = new BrainFuckIDEActions(this);
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final BrainFuckIDEMemoryView memoryView = new BrainFuckIDEMemoryView();
    private final BrainFuckIDEInputOutputView inputOutputView = new BrainFuckIDEInputOutputView(this);

    private BrainFuckIDEExecutor executor = null;

    public BrainFuckIDEWindow() {
        super(TITLE);
        setLayout(new BorderLayout());
        initializeStateSupport();
        initializeTabbedPane();
        initializeMenuBar();
        initializeMemoryView();
        initializeInputOutputView();
    }

    public void initializeStateSupport() {
        addPropertyChangeListener(BRAIN_FUCK_IDE_STATE_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                stateObservable.setChanged();
                stateObservable.notifyObservers(evt.getNewValue());
            }
        });
        firePropertyChange(BRAIN_FUCK_IDE_STATE_PROPERTY, null, BrainFuckIDEState.NONE);
    }

    public void initializeInputOutputView() {
        add(inputOutputView, BorderLayout.SOUTH);
        stateObservable.addObserver(inputOutputView);
        inputOutputView.update(stateObservable, ideState);
    }

    public void initializeMemoryView() {
        add(memoryView, BorderLayout.EAST);
        stateObservable.addObserver(memoryView);
        memoryView.update(stateObservable, ideState);
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(FILE_MENU_CAPTION);
        menu.setMnemonic(KeyEvent.VK_F);
        menu.add(new JMenuItem(actions.getNewAction()));
        menu.add(new JMenuItem(actions.getOpenAction()));
        menu.add(new JSeparator());
        menu.add(new JMenuItem(actions.getSaveAction()));
        menu.add(new JMenuItem(actions.getSaveAsAction()));
        menu.add(new JMenuItem(actions.getCloseAction()));
        menuBar.add(menu);
        menu = new JMenu(RUN_MENU_CAPTION);
        menu.setMnemonic(KeyEvent.VK_U);
        menu.add(new JMenuItem(actions.getRunAction()));
        menu.add(new JMenuItem(actions.getStopAction()));
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void initializeTabbedPane() {
        add(tabbedPane, BorderLayout.CENTER);
    }

    public BrainFuckIDEActions getActions() {
        return actions;
    }

    public BrainFuckIDEState getIdeState() {
        return ideState;
    }

    public void setIdeState(BrainFuckIDEState ideState) {
        firePropertyChange(BRAIN_FUCK_IDE_STATE_PROPERTY, this.ideState, ideState);
        this.ideState = ideState;
    }

    public void addStateObserver(Observer o) {
        stateObservable.addObserver(o);
    }

    public File chooseFileToSave() {
        return chooseFile(ChooseFileType.TO_SAVE);
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

    public void setExecutor(BrainFuckIDEExecutor executor) {
        this.executor = executor;
        if (executor != null)
            memoryView.setInterpreter(executor.getInterpreter());
        else
            memoryView.setInterpreter(null);
    }

    public BrainFuckIDEExecutor getExecutor() {
        return executor;
    }

    public void output(char character) {
        inputOutputView.appendText(character + "");
    }

    public void input(char character) {
        executor.inputInInterpreter(character);
    }

    public BrainFuckIDEDocument getActiveDocument() {
        BrainFuckIDEDocument document;
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent == null) {
            return null;
        } else {
            document = ((BrainFuckIDEDocumentView)selectedComponent).getDocument();
            return document;
        }
    }

    public void addTab() {
        BrainFuckIDEDocument document = new BrainFuckIDEDocument();
        addTab(document);
    }

    public void addTab(BrainFuckIDEDocument document) {
        tabbedPane.addTab(document.getName(), new BrainFuckIDEDocumentView(document));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        setIdeState(BrainFuckIDEState.EDIT);
    }

    public void updateTabTitle() {
        BrainFuckIDEDocument document = getActiveDocument();
        tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), document.getName());
    }

    public void removeActiveTab() {
        tabbedPane.remove(tabbedPane.getSelectedIndex());
        if (tabbedPane.getTabCount() == 0) {
            setIdeState(BrainFuckIDEState.NONE);
        }
    }

    public File chooseFileToOpen() {
        return chooseFile(ChooseFileType.TO_OPEN);
    }

    private class StateObservable extends Observable {
        public void setChanged() {
            super.setChanged();
        }
    }

    private enum ChooseFileType {
        TO_OPEN, TO_SAVE
    }

    public void setRunView() {
        inputOutputView.setEnabled(true);
    }

    public void setDebugView() {
        setRunView();
        memoryView.setVisible(true);
    }

}
