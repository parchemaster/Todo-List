package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.i18n.I18N;

import javax.swing.*;
import java.time.LocalDateTime;

public class TaskDialogFactory implements EntityDialogFactory<Task> {

    private static final I18N I18N = new I18N(TaskDialogFactory.class);

    private final ListModel<Category> categoryListModel;

    public TaskDialogFactory(ListModel<Category> categoryListModel) {
        this.categoryListModel = categoryListModel;
    }

    @Override
    public EntityDialog<Task> newEditDialog(Task task) {
        return new TaskDialog(task, categoryListModel, I18N.getString("editTask"));
    }

    @Override
    public EntityDialog<Task> newAddDialog() {
        var task = new Task(
                I18N.getString("name"),
                I18N.getString("location"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                categoryListModel.getElementAt(0));
        return new TaskDialog(task, categoryListModel, I18N.getString("addTask"));
    }
}