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

    private BrainFuckInterpreter interpreter;
    private final MemoryTableModel memoryTableModel = new MemoryTableModel();
    private final JTable table = new JTable(memoryTableModel);
    private final JScrollPane scrollPane = new JScrollPane(table);

    public BrainFuckIDEMemoryView() {
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

    public void setInterpreter(BrainFuckInterpreter interpreter) {
        this.interpreter = interpreter;
        if (interpreter != null)
            interpreter.addObserver(this);
        memoryTableModel.fireTableStructureChanged();
    }

    @Override
    public void update(Observable observable, Object object) {
        memoryTableModel.fireTableDataChanged();
    }

    private class MemoryTableEditor extends AbstractCellEditor implements TableCellEditor{

        BrainFuckIDECharacterCodeSpinner spinner = new BrainFuckIDECharacterCodeSpinner();

        public MemoryTableEditor() {
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            char realValue = (Character)value;
            spinner.setValue(realValue);
            return spinner;
        }
    }

    private class MemoryTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            char character = (char) value;
            super.setValue(BrainFuckIDEUtils.getCharacterInfo(character));
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
            return interpreter != null ? BrainFuckInterpreter.MEMORY_SIZE : 0;
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
                return  interpreter != null ? interpreter.getMemoryValue(row) : '\0';
            }
        }
    }
}
