package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Subtask;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.dialog.AddSubtaskDialog;
import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.model.SubtaskListModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddSubtaskAction extends AbstractAction {

    private static final I18N I18N = new I18N(AddSubtaskAction.class);

    private final JList<Subtask> subtasks;
    private final Subtask subtask;

    public AddSubtaskAction(JList<Subtask> subtasks, Subtask item, Task task) {
        super(I18N.getString("add"), Icons.ADD_ICON);
        this.subtasks = subtasks;
        this.subtask = item;
        this.subtask.setTask(task);
        putValue(SHORT_DESCRIPTION, I18N.getString("addSubtask"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_N);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl n"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var subtaskListModel = (SubtaskListModel) subtasks.getModel();
        var dialog = new AddSubtaskDialog(subtask);
        dialog.show(subtasks).ifPresent(subtaskListModel::addRow);
    }
}