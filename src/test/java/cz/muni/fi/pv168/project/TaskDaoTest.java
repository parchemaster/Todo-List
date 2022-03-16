package cz.muni.fi.pv168.project;

import cz.muni.fi.pv168.project.data.CategoryDao;
import cz.muni.fi.pv168.project.data.DataAccessException;
import cz.muni.fi.pv168.project.data.SubtaskDao;
import cz.muni.fi.pv168.project.data.TaskDao;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Subtask;
import cz.muni.fi.pv168.project.model.Task;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThat;

final class TaskDaoTest {

    private static EmbeddedDataSource dataSource;
    private static CategoryDao categoryDao;
    private static TaskDao taskDao;
    private static SubtaskDao subtaskDao;

    private static final Category HOME_CAT = new Category("Home");
    private static final Category SCHOOL_CAT = new Category("School");

    private final Task task1 = new Task("Clean the room", "My room", LocalDateTime.now(), LocalDateTime.now(), 1, HOME_CAT);
    private final Task task2 = new Task("Make project", "School", LocalDateTime.now(), LocalDateTime.now(), 1, SCHOOL_CAT);
    private final Task task3 = new Task("Buy some milk", "Store", LocalDateTime.now(), LocalDateTime.now(), 1, HOME_CAT);

    @BeforeAll
    static void initTestDataSource() throws SQLException, IOException {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(System.getProperty("user.home") + "/pv168/db/pv168-todo-list-test");
        dataSource.setCreateDatabase("create");
        categoryDao = new CategoryDao(dataSource);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.CATEGORY");
        }
        categoryDao.create(HOME_CAT);
        categoryDao.create(SCHOOL_CAT);
    }

    @BeforeEach
    void createTaskAndSubtaskDaos() throws SQLException, IOException {
        taskDao = new TaskDao(dataSource, categoryDao::findById);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.TASK");
        }
        subtaskDao = new SubtaskDao(dataSource, taskDao::findById);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.SUBTASK");
        }
    }

    @AfterEach
    void taskAndSubtaskDaosCleanUp() {
        subtaskDao.dropTable();
        taskDao.dropTable();
    }

    @AfterAll
    static void categoryDaoCleanUp() {
        categoryDao.dropTable();
    }

    @Test
    void createTask() {
        taskDao.create(task1);
        assertThat(task1.getId()).isNotNull();
        assertThat(taskDao.findAll()).containsExactly(task1);
    }

    @Test
    void createTaskWithExistingId() {
        task1.setId(123L);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> taskDao.create(task1))
                .withMessage("Task already has ID: %s", task1);
    }

    @Test
    void findAllEmpty() {
        assertThat(taskDao.findAll())
                .isEmpty();
    }

    @Test
    void nullCategory() {
        taskDao.create(task1);
        assertThat(findById(task1.getId())).isEqualTo(task1);
    }

    @Test
    void findAll() {
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.create(task3);
        assertThat(taskDao.findAll()).containsExactlyInAnyOrder(task1, task2, task3);
    }

    @Test
    void delete() {
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.delete(task1);
        assertThat(taskDao.findAll()).containsExactlyInAnyOrder(task2);
    }

    @Test
    void deleteWithNullId() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> taskDao.delete(task1))
                .withMessage("Task has null ID: %s", task1);
    }

    @Test
    void deleteNonExisting() {
        task1.setId(123L);
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> taskDao.delete(task1))
                .withMessage("Failed to delete non-existing task: %s", task1);
    }

    @Test
    void deleteTaskDeletesSubtasks() {
        var subtask1 = new Subtask("Clean the dust", false, task1);
        var subtask2 = new Subtask("Pour the flowers", true, task1);
        var subtask3 = new Subtask("Write the plan", true, task2);

        taskDao.create(task1);
        taskDao.create(task2);
        subtaskDao.create(subtask1);
        subtaskDao.create(subtask2);
        subtaskDao.create(subtask3);

        taskDao.delete(task1);
        assertThat(taskDao.findAll()).containsExactlyInAnyOrder(task2);
        assertThat(subtaskDao.findAll()).containsExactlyInAnyOrder(subtask3);
    }

    @Test
    void update() {
        taskDao.create(task1);
        taskDao.create(task2);

        task1.setTaskName("Prepare some meal");
        task1.setLocation("Kitchen");

        taskDao.update(task1);

        assertThat(findById(task1.getId())).isEqualTo(task1);
        assertThat(findById(task2.getId())).isEqualTo(task2);
    }

    @Test
    void updateWithNullId() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> taskDao.update(task1))
                .withMessage("Task has null ID");
    }

    @Test
    void updateNonExisting() {
        task1.setId(123L);
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> taskDao.update(task1))
                .withMessage("Failed to update non-existing task: %s", task1);
    }

    private Task findById(long id) {
        return taskDao.findAll().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No task with id " + id + " found"));
    }
}