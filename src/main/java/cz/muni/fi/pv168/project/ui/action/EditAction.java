package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class EditAction extends AbstractEntityAction {

    private static final I18N I18N = new I18N(EditAction.class);

    public EditAction(JTabbedPane tabbedPane) {
        super(tabbedPane, selectedRowsCount -> selectedRowsCount == 1);
        putValue(NAME, I18N.getString("edit"));
        putValue(SMALL_ICON, Icons.EDIT_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("editEntry"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var table = getTable();
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }

        var tableModel = getTableModel(); // null
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        var entity = tableModel.getEntity(modelRow);
        try {
            getDialogFactory().newEditDialog(entity)
                    .show(table)
                    .ifPresent(tableModel::updateRow);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    I18N.getString("editEntryError"),
                    I18N.getString("editEntryErrorTitle"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
