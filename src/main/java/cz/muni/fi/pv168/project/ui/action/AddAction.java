package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class AddAction extends AbstractEntityAction {

    private static final I18N I18N = new I18N(AddAction.class);

    public AddAction(JTabbedPane tabbedPane) {
        super(tabbedPane, selectedRowsCount -> true);
        putValue(NAME, I18N.getString("add"));
        putValue(SMALL_ICON, Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("addEntry"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var tableModel = getTableModel();
        try {
            getDialogFactory().newAddDialog()
                    .show(getTable())
                    .ifPresent(tableModel::addRow);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    I18N.getString("addEntryError"),
                    I18N.getString("addEntryErrorTitle"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}