package by.jylilov.brainfuckide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BrainFuckIDEChooseCharacterDialog extends JDialog {
    private static final String CHOOSE_CHARACTER_DIALOG_TITLE = "Choose character";

    private static final int ROW_COUNT = 32;
    private static final int COLUMN_COUNT = 8;

    private final Map<JButton, Character> map = new HashMap<>();
    private final ActionListener buttonsListener = new ButtonActionListener();

    private Character selectedCharacter = null;

    public static Character chooseCharacter() {
        BrainFuckIDEChooseCharacterDialog dialog = new BrainFuckIDEChooseCharacterDialog();
        dialog.setVisible(true);
        return dialog.getSelectedCharacter();
    }

    private BrainFuckIDEChooseCharacterDialog() {
        setLayout(new GridLayout(ROW_COUNT, COLUMN_COUNT));
        initializeButtons();
        initializeFrame();
    }

    private void initializeFrame() {
        setModal(true);
        setTitle(CHOOSE_CHARACTER_DIALOG_TITLE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeButtons() {
        for (int i = 0; i < ROW_COUNT; ++i) {
            for (int j = 0; j < COLUMN_COUNT; ++j) {
                char character = (char) (i * COLUMN_COUNT + j);
                JButton button = new JButton(BrainFuckIDEUtils.getCharacterInfo(character));
                button.addActionListener(buttonsListener);
                map.put(button, character);
                add(button);
            }
        }
    }

    public Character getSelectedCharacter() {
        return selectedCharacter;
    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            selectedCharacter = map.get(button);
            BrainFuckIDEChooseCharacterDialog.this.dispose();
        }
    }
}
