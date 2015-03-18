package by.jylilov.brainfuckide;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class BrainFuckIDEInputOutputView extends JComponent implements Observer {
    public static final String ADD_BUTTON_CAPTION = "Add";

    private final BrainFuckIDEWindow window;

    private final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    private final JPanel inputPane = new JPanel();
    private final BrainFuckIDECharacterCodeSpinner characterCodeSpinner = new BrainFuckIDECharacterCodeSpinner();
    private final JButton characterButton = new JButton(BrainFuckIDEUtils.getCharacterString((char) 0));
    private final JButton addButton = new JButton(ADD_BUTTON_CAPTION);

    private final JTextPane outputTextPane = new JTextPane();

    public BrainFuckIDEInputOutputView(BrainFuckIDEWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        initializeOutputPane();
        initializeSplitPane();
        initializeInputPane();
        add(splitPane);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        addButton.setEnabled(enabled);
        characterButton.setEnabled(enabled);
        characterCodeSpinner.setEnabled(enabled);
    }

    private void initializeInputPane() {
        characterCodeSpinner.addChangeListener(new CharacterCodeSpinnerChangeListener());
        characterButton.addActionListener(new CharacterButtonAction());
        addButton.addActionListener(new AddButtonAction());
        inputPane.setPreferredSize(new Dimension(200, 0));
        inputPane.setLayout(new FlowLayout());
        inputPane.add(characterCodeSpinner);
        inputPane.add(characterButton);
        inputPane.add(addButton);
    }

    private void initializeOutputPane() {
        outputTextPane.setEditable(false);
    }

    private void initializeSplitPane() {
        splitPane.setPreferredSize(new Dimension(0, 150));
        splitPane.setLeftComponent(inputPane);
        splitPane.setRightComponent(outputTextPane);
    }

    @Override
    public void update(Observable o, Object arg) {
        BrainFuckIDEState state = (BrainFuckIDEState)arg;
        setEnabled(state == BrainFuckIDEState.DEBUG || state == BrainFuckIDEState.RUN);
    }

    private class CharacterCodeSpinnerChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            char value = characterCodeSpinner.getValue();
            characterButton.setText(BrainFuckIDEUtils.getCharacterString(value));
        }
    }

    private class CharacterButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Character newValue = BrainFuckIDEChooseCharacterDialog.chooseCharacter();
            if (newValue != null) {
                characterCodeSpinner.setValue(newValue);
            }
        }
    }

    private class AddButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            window.input(characterCodeSpinner.getValue());
        }
    }

    public void appendText(String text) {
        outputTextPane.setText(outputTextPane.getText() + text);
    }

}
