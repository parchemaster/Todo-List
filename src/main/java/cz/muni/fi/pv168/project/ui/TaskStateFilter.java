package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.TaskState;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.model.TaskTableModel;


import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.util.List;

public class TaskStateFilter {

    private final TableRowSorter<TaskTableModel> rowSorter;

    TaskStateFilter(TableRowSorter<TaskTableModel> rowSorter) {
        this.rowSorter = rowSorter;
    }

    void filter(List<TaskState> selectedStatesOfTask) {
        if (selectedStatesOfTask.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(new StateOfTaskRowFilter(selectedStatesOfTask));
        }
    }

    private static class StateOfTaskRowFilter extends RowFilter<TaskTableModel, Integer> {

        private final List<TaskState> selectedStatesOfTask;

        private StateOfTaskRowFilter(List<TaskState> selectedStatesOfTask) {
            this.selectedStatesOfTask = selectedStatesOfTask;
        }

        @Override
        public boolean include(Entry<? extends TaskTableModel, ? extends Integer> entry) {
            TaskTableModel tableModel = entry.getModel();
            int rowIndex = entry.getIdentifier();
            Task task = tableModel.getEntity(rowIndex);
            return selectedStatesOfTask.contains(task.getStateOfTask());
        }
    }
}
