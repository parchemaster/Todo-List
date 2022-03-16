package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public final class DeleteAction extends AbstractEntityAction {

    private static final I18N I18N = new I18N(DeleteAction.class);

    public DeleteAction(JTabbedPane tabbedPane) {
        super(tabbedPane, selectedRowsCount -> selectedRowsCount >= 1);
        putValue(NAME, I18N.getString("delete"));
        putValue(SMALL_ICON, Icons.DELETE_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("deleteEntries"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var table = getTable();
        var selectedRows = table.getSelectedRows();
        int confirmationOption = JOptionPane.showConfirmDialog(table,
                I18N.getString("confirmation"),
                I18N.getString("confirmationTitle"),
                JOptionPane.YES_NO_OPTION);
        if (confirmationOption == JOptionPane.YES_OPTION) {
            try {
                Arrays.stream(selectedRows)
                        .map(table::convertRowIndexToModel)
                        .boxed()
                        .sorted(Comparator.reverseOrder())
                        .forEach(getTableModel()::deleteRow);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        I18N.getString("deleteEntriesError"),
                        I18N.getString("confirmationTitle"),
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
