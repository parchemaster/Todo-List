package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.renderer.LocalDateTimeRenderer;
import cz.muni.fi.pv168.project.ui.dialog.TaskDialogFactory;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialogFactory;
import cz.muni.fi.pv168.project.ui.model.*;


import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDateTime;

public class TaskTablePanel extends JPanel implements EntityTab<Task> {

    private final JTable taskTable;
    private final TaskTableModel taskTableModel;
    private final TaskDialogFactory dialogFactory;

    public TaskTablePanel(TaskTableModel taskTableModel, EntityListModelAdapter<Category> categoryListModel, JPanel stateOfTaskListPanel) {
        this.taskTableModel = taskTableModel;
        this.taskTable = createTaskTable(taskTableModel, categoryListModel);
        dialogFactory = new TaskDialogFactory(categoryListModel);
        setLayout(new BorderLayout());
        taskTable.setRowSorter(new TableRowSorter<>(taskTableModel));
        var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(stateOfTaskListPanel),
                new JScrollPane(taskTable));
        add(splitPane, BorderLayout.CENTER);
    }

    private JTable createTaskTable(TaskTableModel tableModel, ListModel<Category> categoryListModel) {
        var table = new JTable(tableModel);
        table.setRowHeight(25);
        var categoryJComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        table.setDefaultEditor(Category.class, new DefaultCellEditor(categoryJComboBox));
        table.setDefaultRenderer(LocalDateTime.class, new LocalDateTimeRenderer());
        return table;
    }

    @Override
    public EditableModel<Task> getEditableModel() {
        return taskTableModel;
    }

    @Override
    public EntityDialogFactory<Task> getEntityDialogFactory() {
        return dialogFactory;
    }

    @Override
    public int getSelectedRowsCount() {
        return taskTable.getSelectedRowCount();
    }

    public JTable getTable() {
        return taskTable;
    }
}
