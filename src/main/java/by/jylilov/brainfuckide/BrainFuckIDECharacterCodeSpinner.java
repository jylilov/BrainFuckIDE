package by.jylilov.brainfuckide;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class BrainFuckIDECharacterCodeSpinner extends JComponent {
    private static final int DEFAULT_VALUE = 0;
    private static final int MINIMAL_VALUE = 0;
    private static final int MAXIMUM_VALUE = 255;
    private static final int STEP = 1;

    private final SpinnerModel model = new SpinnerNumberModel(
            DEFAULT_VALUE, MINIMAL_VALUE, MAXIMUM_VALUE, STEP);

    public void addChangeListener(ChangeListener changeListener) {
        model.addChangeListener(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        model.removeChangeListener(changeListener);
    }

    private final JSpinner spinner = new JSpinner(model);

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        spinner.setEnabled(enabled);
    }

    public BrainFuckIDECharacterCodeSpinner() {
        setLayout(new BorderLayout());
        add(spinner);
    }

    public void setValue(char value) {
        model.setValue((int)value);
    }

    public char getValue() {
        int modelValue = (Integer)model.getValue();
        return (char)modelValue;
    }
}
