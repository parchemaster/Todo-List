package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.eu.hansolo.custom.SteelCheckBox;
import cz.muni.fi.pv168.project.eu.hansolo.tools.ColorDef;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.TaskState;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.model.LocalDateModel;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.time.LocalDateTime;

final class TaskDialog extends EntityDialog<Task> {

    private static final I18N I18N = new I18N(TaskDialog.class);

    private final JTextField taskName = new JTextField(20);
    private final JTextField taskLocation = new JTextField(20);
    private final ComboBoxModel<Category> categoryModel;
    private final DateModel<LocalDateTime> deadline = new LocalDateModel();
    private final JSpinner estimatedTime = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final SteelCheckBox inProgress = createInProgressButton();

    private final Task task;

    public TaskDialog(Task task, ListModel<Category> departmentModel, String title) {
        super(title);
        this.task = task;
        this.categoryModel = new ComboBoxModelAdapter<>(departmentModel);
        setValues();
        addFields();
    }

    private void setValues() {
        taskName.setText(task.getTaskName());
        taskLocation.setText(task.getLocation());
        categoryModel.setSelectedItem(task.getCategory());
        deadline.setValue(task.getDeadline());
        estimatedTime.setValue(task.getEstimatedTime());
        inProgress.setSelected(task.getStateOfTask() == TaskState.IN_PROGRESS);
        inProgress.setEnabled(task.getStateOfTask() == TaskState.WAITING);
    }

    private void addFields() {
        add(I18N.getString("name"), taskName);
        add(I18N.getString("location"), taskLocation);
        add(I18N.getString("deadline"), new JDatePicker(deadline));
        add(I18N.getString("category"), new JComboBox<>(categoryModel));
        add(I18N.getString("estimatedTime"), estimatedTime);
        add(I18N.getString("inProgress"), inProgress);
    }


    @Override
    Task getEntity() {
        task.setTaskName(taskName.getText());
        task.setLocation(taskLocation.getText());
        task.setCategory((Category) categoryModel.getSelectedItem());
        task.setDeadline(deadline.getValue());
        task.setEstimatedTime(Integer.parseInt(estimatedTime.getValue().toString()));
        task.setDoneTime(task.getDoneTime());
        task.setStartTime(task.getStartTime());
        if (task.getStateOfTask() != TaskState.FINISHED)
            task.setStateOfTask(inProgress.isSelected() ? TaskState.IN_PROGRESS : TaskState.WAITING);
        return task;
    }

    private SteelCheckBox createInProgressButton() {
        SteelCheckBox button = new SteelCheckBox();
        button.setSelectedColor(ColorDef.CYAN);
        button.setRised(false);
        button.setText(" ");
        return button;
    }
}
