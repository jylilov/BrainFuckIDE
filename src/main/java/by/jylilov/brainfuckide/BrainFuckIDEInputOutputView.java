package by.jylilov.brainfuckide;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrainFuckIDEInputOutputView extends JComponent {
    public static final String ADD_BUTTON_CAPTION = "Add";

    private final BrainFuckIDEWindow window;

    private final BrainFuckIDECharacterCodeSpinner characterCodeSpinner = new BrainFuckIDECharacterCodeSpinner();
    private final JButton characterButton = new JButton(BrainFuckIDEUtils.getCharacterString((char) 0));
    private final JButton addButton = new JButton(ADD_BUTTON_CAPTION);

    private final JTextPane outputTextPane = new JTextPane();
    private final JPanel inputTextPane = new JPanel();
    private final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    public BrainFuckIDEInputOutputView(BrainFuckIDEWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        initializeOutputPane();
        initializeSplitPane();
        initializeInputPane();
        add(splitPane);
    }

    private void initializeInputPane() {
        characterCodeSpinner.addChangeListener(new CharacterCodeSpinnerChangeListener());
        characterButton.addActionListener(new CharacterButtonAction());
        addButton.addActionListener(new AddButtonAction());
        inputTextPane.setPreferredSize(new Dimension(200, 0));
        inputTextPane.setLayout(new FlowLayout());
        inputTextPane.add(characterCodeSpinner);
        inputTextPane.add(characterButton);
        inputTextPane.add(addButton);
    }

    private void initializeOutputPane() {
        outputTextPane.setEditable(false);
    }

    private void initializeSplitPane() {
        splitPane.setPreferredSize(new Dimension(0, 150));
        splitPane.setLeftComponent(inputTextPane);
        splitPane.setRightComponent(outputTextPane);
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
