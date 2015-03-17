package by.jylilov.brainfuckide;

import by.jylilov.brainfuck.BrainFuckInterpreter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class BrainFuckIDEMemoryView extends JComponent implements Observer{

    private final BrainFuckInterpreter interpreter;
    private final MemoryTableModel memoryTableModel = new MemoryTableModel();
    private final JTable table = new JTable(memoryTableModel);
    private final JScrollPane scrollPane = new JScrollPane(table);

    public BrainFuckIDEMemoryView(BrainFuckInterpreter interpreter) {
        this.interpreter = interpreter;
        initializeTable();
        initializeScrollPane();
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    public void initializeScrollPane() {
        scrollPane.setPreferredSize(new Dimension(200, 0));
    }

    public void initializeTable() {
        table.setDefaultEditor(Character.class, new MemoryTableEditor());
        table.setDefaultRenderer(Character.class, new MemoryTableCellRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    @Override
    public void update(Observable observable, Object object) {
        memoryTableModel.fireTableDataChanged();
    }

    private class MemoryTableEditor extends AbstractCellEditor implements TableCellEditor{
        private static final int DEFAULT_SPINNER_VALUE = 0;
        private static final int MINIMAL_SPINNER_VALUE = 0;
        private static final int MAXIMUM_SPINNER_VALUE = 255;
        private static final int SPINNER_STEP = 1;

        private final SpinnerModel model = new SpinnerNumberModel(
                DEFAULT_SPINNER_VALUE, MINIMAL_SPINNER_VALUE, MAXIMUM_SPINNER_VALUE, SPINNER_STEP);
        private final JSpinner spinner = new JSpinner(model);

        public MemoryTableEditor() {
        }

        @Override
        public Object getCellEditorValue() {
            int realValue = (Integer)model.getValue();
            return (char)realValue;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            char realValue = (Character)value;
            model.setValue((int)realValue);
            return spinner;
        }
    }

    private class MemoryTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            char character = (char) value;
            super.setValue((int)character + " (\"" + character + "\")");
        }
    }

    private class MemoryTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES = {"Index", "Value"};

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public int getRowCount() {
            return BrainFuckInterpreter.MEMORY_SIZE;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) {
                return Character.class;
            } else {
                return super.getColumnClass(columnIndex);
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            interpreter.setMemoryValue(rowIndex, (Character)value);
        }

        @Override
        public Object getValueAt(int row, int column) {
            if (column == 0) {
                return row;
            } else {
                return interpreter.getMemoryValue(row);
            }
        }
    }
}
