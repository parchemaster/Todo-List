package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.data.SubtaskDao;
import cz.muni.fi.pv168.project.model.Subtask;
import cz.muni.fi.pv168.project.ui.error.ErrorDialog;
import cz.muni.fi.pv168.project.ui.i18n.I18N;

import javax.swing.*;
import java.util.List;

public class SubtaskListModel extends AbstractListModel<Subtask> {

    private static final I18N I18N = new I18N(SubtaskListModel.class);

    private final List<Subtask> subtasks;
    private final SubtaskDao subtaskDao;

    public SubtaskListModel(List<Subtask> subtasks, SubtaskDao subtaskDao) {
        this.subtasks = subtasks;
        this.subtaskDao = subtaskDao;
    }

    public List<Subtask> getTaskList() {
        return subtasks;
    }


    @Override
    public int getSize() {
        return subtasks.size();
    }

    @Override
    public Subtask getElementAt(int rowIndex) {
        return subtasks.get(rowIndex);
    }


    public void addRow(Subtask subtask) {
        try {
            subtaskDao.create(subtask);
            fireIntervalAdded(subtask, this.getSize(), this.getSize() + 1);
            getTaskList().add(subtask);
        } catch (Exception ex) {
            ErrorDialog.show(I18N.getString("createSubtaskError"), ex);
        }
    }
}
