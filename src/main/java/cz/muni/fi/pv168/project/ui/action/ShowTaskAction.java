package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.data.SubtaskDao;
import cz.muni.fi.pv168.project.data.TaskDao;
import cz.muni.fi.pv168.project.eu.hansolo.custom.SteelCheckBox;
import cz.muni.fi.pv168.project.eu.hansolo.tools.ColorDef;
import cz.muni.fi.pv168.project.model.TaskState;
import cz.muni.fi.pv168.project.model.Subtask;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.renderer.CheckboxListCellRenderer;
import cz.muni.fi.pv168.project.ui.dialog.SpentTimeDialog;
import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.model.SubtaskListModel;
import cz.muni.fi.pv168.project.ui.renderer.LocalDateTimeRenderer;
import cz.muni.fi.pv168.project.ui.resources.Icons;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.util.List;

public class ShowTaskAction extends JPanel{

    private static final I18N I18N = new I18N(ShowTaskAction.class);

    private final Task task;
    private final SubtaskListModel subtaskListModel;
    private final JLabel statsLabel;
    private final SteelCheckBox inProgressButton;
    private final JButton addSubtaskButton;
    private final TaskDao taskDao;
    private final SubtaskDao subtaskDao;
    private final JList<Subtask> subtaskCheckBoxList;

    public ShowTaskAction(Task task, JPanel leftPanel, SubtaskDao subtaskDao, TaskDao taskDao, Action backAction, JToolBar toolBar) {
        this.task = task;
        this.taskDao = taskDao;
        this.subtaskDao = subtaskDao;
        this.statsLabel = createLabel("stats", String.valueOf(task.getProgress()), Icons.PROGRESS_ICON);

        List<Subtask> subtaskList = subtaskDao.findByTaskId(task.getId());
        this.subtaskListModel = new SubtaskListModel(subtaskList, subtaskDao);
        this.subtaskCheckBoxList = createSubtaskCheckBoxList(subtaskList);
        this.addSubtaskButton = createAddSubtaskButton();

        this.inProgressButton = createInProgressButton();

        toolBar.setVisible(false);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JButton(backAction));
        add(createFinishedButton());
        add(createInfoPanel(), BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(subtaskCheckBoxList, BorderLayout.WEST);
        add(new JScrollPane(panel), BorderLayout.SOUTH);

        leftPanel.remove(0);
        leftPanel.add(new JList<>());
    }

    private JList<Subtask> createSubtaskCheckBoxList(List<Subtask> subtaskList) {
        var subtaskCheckBoxList = new JList<>(subtaskListModel);
        subtaskCheckBoxList.setCellRenderer(new CheckboxListCellRenderer());
        subtaskCheckBoxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subtaskCheckBoxList.setBackground(new Color(214, 217, 223));
        subtaskCheckBoxList.addMouseListener(checkSubtask(subtaskList));
        subtaskCheckBoxList.setEnabled(task.getStateOfTask() != TaskState.FINISHED);
        return subtaskCheckBoxList;
    }

    private JButton createAddSubtaskButton() {
        var addSubtaskButton = new JButton(I18N.getString("addSubtaskButton"));
        addSubtaskButton.setEnabled(task.getStateOfTask() != TaskState.FINISHED);
        addSubtaskButton.addActionListener(new AddSubtaskAction(subtaskCheckBoxList, new Subtask(), task));
        return addSubtaskButton;
    }

    private SteelCheckBox createInProgressButton() {
        SteelCheckBox button = new SteelCheckBox();
        button.setSelectedColor(ColorDef.CYAN);
        button.setRised(false);
        button.setSelected(task.getStateOfTask() == TaskState.IN_PROGRESS);
        button.setEnabled(task.getStateOfTask() == TaskState.WAITING);
        button.setText(I18N.getString("inProgress"));
        button.addActionListener(pressInProgressButton(button));
        return button;
    }

    private JToggleButton createFinishedButton() {
        var button = new JCheckBox();
        button.setPreferredSize(new Dimension(24,30));
        button.setText(I18N.getString("finishConfirmation"));
        if (task.getStateOfTask() == TaskState.FINISHED)
            button.setText(I18N.getString("finished"));
        button.setSelected(task.getStateOfTask() == TaskState.FINISHED);
        button.setEnabled(task.getStateOfTask() != TaskState.FINISHED);
        button.addActionListener(pressFinishedButton(button));
        return button;
    }

    private ActionListener pressFinishedButton(JToggleButton finishedButton) {
        return e -> {
            finishedButton.setText(I18N.getString("finished"));
            finishedButton.setEnabled(false);
            inProgressButton.setEnabled(false);
            addSubtaskButton.setEnabled(false);
            task.setDoneTime(LocalDateTime.now());
            task.setStateOfTask(TaskState.FINISHED);
            task.setProgress(100);
            var timeDialog = new SpentTimeDialog();
            timeDialog.show(this);
            task.setSpentTime(timeDialog.getTime());
            taskDao.update(task);
        };
    }

    private ActionListener pressInProgressButton(SteelCheckBox inProgressButton) {
        return e -> {
            if (inProgressButton.isSelected()) {
                task.setStateOfTask(TaskState.IN_PROGRESS);
                taskDao.update(task);
            }
            else {
                task.setStateOfTask(TaskState.WAITING);
                taskDao.update(task);
            }
        };
    }

    private MouseListener checkSubtask(List<Subtask> subtaskList) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList<?> list = (JList<?>) event.getSource();
                int index = list.locationToIndex(event.getPoint());// Get index of item
                // clicked
                Subtask subtask = (Subtask) list.getModel()
                        .getElementAt(index);
                subtask.setDone(!subtask.isDone()); // Toggle selected state
                subtaskDao.update(subtask);
                task.setProgress(subtaskList);
                taskDao.update(task);
                list.repaint(list.getCellBounds(index, index));// Repaint cell
                setNewProgress(String.valueOf(task.getProgress()));
            }
        };
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(addSubtaskButton);
        return panel;
    }


    private JPanel createInfoPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        jPanel.add(createLabel("name", task.getTaskName(), Icons.TASK_NAME_ICON));
        jPanel.add(createLabel("category", task.getCategory().toString(), Icons.CATEGORY_ICON));
        jPanel.add(createLabel("location", task.getLocation(), Icons.LOCATION_ICON));
        jPanel.add(createLabel("startDay", LocalDateTimeRenderer.formatDate(task.getStartTime()), Icons.START_DAY_ICON));
        jPanel.add(createLabel("deadline", LocalDateTimeRenderer.formatDate(task.getDeadline()), Icons.DEADLINE_ICON));
        jPanel.add(createLabel("estimatedTime", String.valueOf(task.getEstimatedTime()), Icons.ESTIMATED_TIME_ICON));
        jPanel.add(statsLabel);
        jPanel.add(inProgressButton);
        jPanel.add(createButtonPanel());
        return jPanel;
    }

    private JLabel createLabel(String localeName, String text, Icon icon) {
        var label = new JLabel();
        label.setText("<html>"+ I18N.getString(localeName) +" <i>" + text + "</i></html>");
        label.setIcon(icon);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setFont(Font.getFont(Font.DIALOG_INPUT));
        return label;
    }

    private void setNewProgress(String newProgress) {
        statsLabel.setText("<html>"+ I18N.getString("stats") +" <i>" + newProgress + "</i></html>");
    }
}
