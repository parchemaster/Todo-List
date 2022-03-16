package cz.muni.fi.pv168.project;

import cz.muni.fi.pv168.project.data.*;
import cz.muni.fi.pv168.project.ui.MainWindow;
import cz.muni.fi.pv168.project.ui.action.QuitAction;
import cz.muni.fi.pv168.project.ui.error.UncaughtExceptionHandler;
import cz.muni.fi.pv168.project.ui.i18n.I18N;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    private static final I18N I18N = new I18N(Main.class);
    private static TaskDao taskDao;
    private static CategoryDao categoryDao;
    private static SubtaskDao subtaskDao;


    public static void main(String[] args){
        try {
            initNimbusLookAndFeel();
            var dataSource = createDataSource();
            EventQueue.invokeAndWait(() ->
                    Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler()));
            categoryDao = new CategoryDao(dataSource);
            CategoryTableManager.initTable(categoryDao);
            taskDao = new TaskDao(dataSource, categoryDao::findById);
            TaskTableManager.initTable(taskDao, categoryDao::findByName);
            subtaskDao = new SubtaskDao(dataSource, taskDao::findById);
            SubtaskTableManager.initTable(subtaskDao);
            EventQueue.invokeAndWait(() -> {
                Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler());
                new MainWindow(taskDao, categoryDao, subtaskDao).show();
            });
        } catch (Exception ex){
            showError(ex);
        }
    }

    private static void showError(Exception ex) {
        EventQueue.invokeLater(() -> {
            ex.printStackTrace();
            Object[] options = {
                    new JButton(new QuitAction())
            };
            JOptionPane.showOptionDialog(null,
                    I18N.getString("initializationFailedDialogText"),
                    I18N.getString("initializationFailedDialogTitle"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                    null, options, options[0]);
        });
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/pv168/db/pv168-todo-list";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

    private static void initNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Nimbus layout initialization failed", ex);
        }
    }
}
