package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.data.TaskDao;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.error.ErrorDialog;
import cz.muni.fi.pv168.project.ui.i18n.I18N;


import java.time.LocalDateTime;
import java.util.List;

public class TaskTableModel extends AbstractEntityTableModel<Task> implements EditableModel<Task> {

    private static final I18N I18N = new I18N(TaskTableModel.class);

    private static final List<Column<Task, ?>> COLUMNS = List.of(
            Column.readOnly(I18N.getString("name"), String.class, Task::getTaskName),
            Column.readOnly(I18N.getString("location"), String.class, Task::getLocation),
            Column.readOnly(I18N.getString("deadline"), LocalDateTime.class, Task::getDeadline),
            Column.readOnly(I18N.getString("category"), Category.class, Task::getCategory),
            Column.readOnly(I18N.getString("estimatedTime"), Integer.class, Task::getEstimatedTime),
            Column.readOnly(I18N.getString("progress"), Integer.class, Task::getProgress)
    );

    private final List<Task> tasks;
    private final TaskDao taskDao;

    public TaskTableModel(List<Task> tasks, TaskDao taskDao) {
        super(COLUMNS);
        this.tasks = tasks;
        this.taskDao = taskDao;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public void deleteRow(int rowIndex) {
        taskDao.delete(getEntity(rowIndex));
        tasks.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    @Override
    public void addRow(Task task) {
        try {
            int newRowIndex = tasks.size();
            taskDao.create(task);
            tasks.add(task);
            fireTableRowsInserted(newRowIndex, newRowIndex);
        } catch (Exception ex) {
            ErrorDialog.show(I18N.getString("createTaskError"), ex);
        }
    }

    @Override
    public void updateRow(Task task) {
        try {
            int rowIndex = tasks.indexOf(task);
            taskDao.update(task);
            fireTableRowsUpdated(rowIndex, rowIndex);
        } catch (Exception ex) {
            ErrorDialog.show(I18N.getString("updateTaskError"), ex);
        }
    }

    @Override
    public Task getEntity(int rowIndex) {
        return tasks.get(rowIndex);
    }
}
