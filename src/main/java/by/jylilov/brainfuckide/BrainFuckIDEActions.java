package by.jylilov.brainfuckide;

import by.jylilov.brainfuck.BrainFuckInterpreter;
import by.jylilov.brainfuck.BrainFuckInterpreterStart;
import by.jylilov.brainfuck.BrainFuckUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.*;

public class BrainFuckIDEActions {
    private static final String NEW_ACTION_CAPTION = "New";
    private static final String OPEN_ACTION_CAPTION = "Open";
    private static final String SAVE_ACTION_CAPTION = "Save";
    private static final String SAVE_AS_ACTION_CAPTION = "Save as";
    private static final String CLOSE_ACTION_CAPTION = "Close";
    private static final String RUN_ACTION_CAPTION = "Run";
    private static final String STOP_ACTION_CAPTION = "Stop";
    private static final String DEBUG_ACTION_CAPTION = "Debug";
    private static final String STEP_ACTION_CAPTION = "Step";
    public static final String COMPILE_ACTION_CAPTION = "Compile";

    private final BrainFuckIDEWindow window;

    private final AbstractBrainFuckIDEAction newAction;
    private final AbstractBrainFuckIDEAction saveAction;
    private final AbstractBrainFuckIDEAction saveAsAction;
    private final AbstractBrainFuckIDEAction closeAction;
    private final AbstractBrainFuckIDEAction openAction;
    private final AbstractBrainFuckIDEAction runAction;
    private final AbstractBrainFuckIDEAction stopAction;
    private final AbstractBrainFuckIDEAction debugAction;
    private final AbstractBrainFuckIDEAction stepAction;
    private final AbstractBrainFuckIDEAction compileAction;

    public BrainFuckIDEActions(BrainFuckIDEWindow window) {
        this.window = window;
        newAction = new NewAction();
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        closeAction = new CloseAction();
        openAction = new OpenAction();
        runAction = new RunAction();
        stopAction = new StopAction();
        debugAction = new DebugAction();
        stepAction = new StepAction();
        compileAction = new CompileAction();
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

    public AbstractBrainFuckIDEAction getDebugAction() {
        return debugAction;
    }

    public AbstractBrainFuckIDEAction getStepAction() {
        return stepAction;
    }

    public AbstractBrainFuckIDEAction getCompileAction() {
        return compileAction;
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
            setEnabled(ideState == BrainFuckIDEState.EDIT || ideState == BrainFuckIDEState.DEBUG);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            window.setIdeState(BrainFuckIDEState.RUN);
            if (window.getExecutor() == null) {
                runProgram();
            }
        }
    }

    private void runProgram() {
        String sourceCode = window.getActiveDocument().getSourceCode();
        BrainFuckInterpreter interpreter = new BrainFuckInterpreter(sourceCode);
        BrainFuckIDEExecutor executor = new BrainFuckIDEExecutor(interpreter, window);
        window.setExecutor(executor);
        executor.execute();
    }

    private class DebugAction extends AbstractBrainFuckIDEAction {
        public DebugAction() {
            putValue(NAME, DEBUG_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            window.setIdeState(BrainFuckIDEState.DEBUG);
            runProgram();
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
            window.clearHighlight();
        }
    }

    class StepAction extends AbstractBrainFuckIDEAction {
        public StepAction() {
            putValue(NAME, STEP_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_E);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.DEBUG);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            window.getExecutor().nextOperation();
        }
    }

    private class CompileAction extends AbstractBrainFuckIDEAction {

        private static final String START_CLASS = "by.jylilov.brainfuck.BrainFuckInterpreterStart";
        private static final String MANIFEST_VERSION = "1.0";

        private static final String PACKAGE = "by/jylilov/brainfuck/";

        public CompileAction() {
            putValue(NAME, COMPILE_ACTION_CAPTION);
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        }

        @Override
        public void update(BrainFuckIDEState ideState) {
            setEnabled(ideState == BrainFuckIDEState.EDIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            File newJarFile = window.chooseFileToSave();
            if (newJarFile == null) return;
            try {
                Manifest manifest = createManifest();
                URL url = BrainFuckInterpreterStart.class.getClassLoader().getResource(PACKAGE);
                if (url.getProtocol().equals("jar")) {
                    JarFile currentJarFile = getCurrentJarFile(url);
                    writeJarFileFromJar(newJarFile, manifest, currentJarFile);
                } else {
                    File directory = new File(url.getFile());
                    writeJarFileFromDirectory(newJarFile,  manifest, directory);
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        private JarFile getCurrentJarFile(URL url) throws IOException {
            String filePath = url.getPath().replaceAll(".+:", "").replaceAll("!.+", "");
            return new JarFile(filePath);
        }

        private void writeClassEntriesFromJarFile(JarFile currentJarFile, JarOutputStream jarOutputStream) throws IOException {
            Enumeration<JarEntry> entries = currentJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(PACKAGE)) {
                    jarOutputStream.putNextEntry(entry);
                    BrainFuckUtils.writeInputToOutputStream(currentJarFile.getInputStream(entry), jarOutputStream);
                    jarOutputStream.closeEntry();
                }
            }
        }

        private void writeJarFileFromJar(File newJarFile, Manifest manifest, JarFile currentJarFile) throws IOException {
            JarOutputStream jarOutputStream = createJarOutputStream(newJarFile, manifest);
            try {
                writeClassEntriesFromJarFile(currentJarFile, jarOutputStream);
                writeResource(jarOutputStream);
            } finally {
                jarOutputStream.close();
            }
        }

        private void writeJarFileFromDirectory(File newJarFile, Manifest manifest, File directory) throws IOException {
            JarOutputStream jarOutputStream = createJarOutputStream(newJarFile, manifest);
            try {
                writeClassEntriesFromDirectory(directory, jarOutputStream);
                writeResource(jarOutputStream);
            } finally {
                jarOutputStream.close();
            }

        }

        private void writeResource(JarOutputStream jarOutputStream) throws IOException {
            JarEntry jarEntry = new JarEntry(BrainFuckInterpreterStart.RESOURCE_SOURCE_CODE_NAME);
            jarOutputStream.putNextEntry(jarEntry);
            jarOutputStream.write(window.getActiveDocument().getSourceCode().getBytes());
            jarOutputStream.closeEntry();
        }

        private JarOutputStream createJarOutputStream(File newJarFile, Manifest manifest) throws IOException {
            FileOutputStream fileOutputStream = new FileOutputStream(newJarFile);
            return new JarOutputStream(fileOutputStream, manifest);
        }

        private Manifest createManifest() {
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION);
            manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, START_CLASS);
            return manifest;
        }

        private void writeClassEntriesFromDirectory(File directory, JarOutputStream jarOutputStream) throws IOException {
            for (File file: directory.listFiles()) {
                JarEntry entry = new JarEntry(PACKAGE + file.getName());
                jarOutputStream.putNextEntry(entry);
                BrainFuckUtils.writeInputToOutputStream(new FileInputStream(file), jarOutputStream);
                jarOutputStream.closeEntry();
            }
        }

    }

}
