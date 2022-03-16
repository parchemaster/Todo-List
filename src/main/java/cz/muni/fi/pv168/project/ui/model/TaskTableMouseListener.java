package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.data.SubtaskDao;
import cz.muni.fi.pv168.project.data.TaskDao;
import cz.muni.fi.pv168.project.ui.action.ShowTaskAction;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TaskTableMouseListener extends MouseAdapter implements MouseListener {

    private final TaskTableModel taskTableModel;
    private final JPanel leftPanel;
    private final SubtaskDao subtaskDao;
    private final TaskDao taskDao;
    private final JTabbedPane tabbedPane;
    private final Action back;
    private final JToolBar toolBar;

    public TaskTableMouseListener(TaskTableModel taskTableModel, JPanel leftPanel, SubtaskDao subtaskDao, TaskDao taskDao, JTabbedPane tabbedPane, Action back, JToolBar toolBar) {
        this.taskTableModel = taskTableModel;
        this.leftPanel = leftPanel;
        this.subtaskDao = subtaskDao;
        this.taskDao = taskDao;
        this.tabbedPane = tabbedPane;
        this.back = back;
        this.toolBar = toolBar;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        var tasksTable = (JTable) mouseEvent.getSource();
        if (mouseEvent.getClickCount() == 2) {
            int index = tasksTable.getRowSorter().convertRowIndexToModel(tasksTable.rowAtPoint(mouseEvent.getPoint()));
            if (index >= 0) {
                var showTask = new ShowTaskAction(taskTableModel.getEntity(index), leftPanel, subtaskDao, taskDao, back, toolBar);
                tabbedPane.setComponentAt(0, showTask);
            }
        }
    }
}
