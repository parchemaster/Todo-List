package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.CategoryDao;
import cz.muni.fi.pv168.project.data.SubtaskDao;
import cz.muni.fi.pv168.project.data.TaskDao;
import cz.muni.fi.pv168.project.model.TaskState;

import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.renderer.TaskStateRenderer;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {

    private static final I18N I18N = new I18N(MainWindow.class);

    private final JFrame frame;

    private final Action quitAction = new QuitAction();

    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;


    public MainWindow(TaskDao taskDao, CategoryDao categoryDao, SubtaskDao subtaskDao){
        var tabbedPane = new JTabbedPane();

        var categoryList = new ArrayList<>(categoryDao.findAll());
        var taskList = new ArrayList<>(taskDao.findAll());

        var stateOfTaskList = createStateOfTaskList();
        stateOfTaskList.setCellRenderer(new TaskStateRenderer());
        var stateOfTaskListPanel = new JPanel();
        stateOfTaskListPanel.add(stateOfTaskList);
        stateOfTaskList.setBackground(new Color(214, 217, 223));

        var categoryTableModel = new CategoryTableModel(categoryList, categoryDao, taskList);
        var categoryTablePanel = new CategoryTablePanel(categoryTableModel);
        var categoryListModel = new EntityListModelAdapter<>(categoryTableModel);
        var taskTableModel = new TaskTableModel(taskList, taskDao);
        var taskTablePanel = new TaskTablePanel(taskTableModel, categoryListModel, stateOfTaskListPanel);
        var taskTable = taskTablePanel.getTable();

        addAction = new AddAction(tabbedPane);
        deleteAction = new DeleteAction(tabbedPane);
        editAction = new EditAction(tabbedPane);

        JToolBar toolBar = createToolbar();

        Action backAction = new ActionBack(taskTablePanel, tabbedPane, stateOfTaskList, stateOfTaskListPanel, toolBar);
        var mouseModel = new TaskTableMouseListener(taskTableModel, stateOfTaskListPanel, subtaskDao, taskDao, tabbedPane, backAction, toolBar);
        taskTable.addMouseListener(mouseModel);

        configureStateOfTaskFilter(taskTableModel, taskTable, stateOfTaskList);

        tabbedPane.addTab(I18N.getString("tasksTab"), Icons.TASK_ICON, taskTablePanel);
        tabbedPane.addTab(I18N.getString("categoriesTab"), Icons.CATEGORY_ICON, categoryTablePanel);

        this.frame = createFrame();
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private JList<TaskState> createStateOfTaskList() {
        var stateOfTaskListModel = new DefaultListModel<TaskState>();
        stateOfTaskListModel.addAll(List.of(TaskState.values()));
        return new JList<>(stateOfTaskListModel);
    }

    private void configureStateOfTaskFilter(TaskTableModel taskTableModel, JTable tasksTable, JList<TaskState> stateOfTaskList) {
        var rowSorter = new TableRowSorter<>(taskTableModel);
        var stateOfTaskFilter = new TaskStateFilter(rowSorter);
        tasksTable.setRowSorter(rowSorter);
        stateOfTaskList.addListSelectionListener(e -> stateOfTaskFilter.filter(stateOfTaskList.getSelectedValuesList()));
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame(I18N.getString("app"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,500));
        return frame;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        toolbar.add(quitAction);
        toolbar.addSeparator();
        toolbar.add(addAction);
        toolbar.add(deleteAction);
        toolbar.add(editAction);
        return toolbar;
    }

}
