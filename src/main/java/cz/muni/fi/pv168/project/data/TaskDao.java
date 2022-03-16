package cz.muni.fi.pv168.project.data;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.TaskState;
import cz.muni.fi.pv168.project.model.Task;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TaskDao implements DataAccessObject<Task>{

    private final DataSource dataSource;
    private final Function<Long, Category> categoryResolver;

    public TaskDao(DataSource dataSource,
                   Function<Long, Category> categoryResolver){
        this.dataSource = dataSource;
        this.categoryResolver = categoryResolver;
    }

    @Override
    public void create(Task entity) throws DataAccessException {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("Task already has ID: " + entity);
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO TASK(NAME, PLACE, DEADLINE, START_DATE, HOURS_NEEDED, CATEGORY_ID, PROGRESS, DONE_TIME, STATE, SPENT_TIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, entity.getTaskName());
            st.setString(2, entity.getLocation());
            st.setTimestamp(3, Timestamp.valueOf(entity.getDeadline()));
            st.setTimestamp(4, Timestamp.valueOf(entity.getStartTime()));
            st.setInt(5, entity.getEstimatedTime());
            if (entity.getCategory() == null) {
                st.setNull(6, Types.BIGINT);
            } else {
                st.setLong(6, entity.getCategory().getId());
            }
            st.setInt(7, 0);
            st.setTimestamp(8, Timestamp.valueOf(entity.getDoneTime()));
            st.setString(9, TaskState.WAITING.name());
            st.setInt(10, 0);
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() != 1) {
                    throw new DataAccessException("Failed to fetch generated key: compound key returned for task: " + entity);
                }
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for task: " + entity);
                }
                if (rs.next()) {
                    throw new DataAccessException("Failed to fetch generated key: multiple keys returned for task: " + entity);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store task " + entity, ex);
        }
    }

    @Override
    public Collection<Task> findAll() throws DataAccessException {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, PLACE, DEADLINE, START_DATE, HOURS_NEEDED, CATEGORY_ID, PROGRESS, STATE, DONE_TIME, SPENT_TIME FROM TASK")) {

            List<Task> tasks = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var task = new Task(
                            rs.getString("NAME"),
                            rs.getString("PLACE"),
                            rs.getTimestamp("DEADLINE").toLocalDateTime(),
                            rs.getTimestamp("START_DATE").toLocalDateTime(),
                            rs.getInt("HOURS_NEEDED"),
                            categoryResolver.apply(rs.getLong("CATEGORY_ID")));
                    task.setProgress(rs.getInt("PROGRESS"));
                    task.setId(rs.getLong("ID"));
                    task.setDoneTime(rs.getTimestamp("DONE_TIME").toLocalDateTime());
                    task.setStateOfTask(TaskState.valueOf(rs.getString("STATE")));
                    task.setSpentTime(rs.getInt("SPENT_TIME"));
                    tasks.add(task);
                }
            }
            return tasks;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all tasks", ex);
        }
    }

    public Task findById(Long id) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT NAME, PLACE, DEADLINE, START_DATE, HOURS_NEEDED, CATEGORY_ID, PROGRESS, DONE_TIME, STATE, SPENT_TIME FROM TASK WHERE ID = ?")) {
            st.setLong(1, id);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var task = new Task(
                            rs.getString("NAME"),
                            rs.getString("PLACE"),
                            rs.getTimestamp("DEADLINE").toLocalDateTime(),
                            rs.getTimestamp("START_DATE").toLocalDateTime(),
                            rs.getInt("HOURS_NEEDED"),
                            categoryResolver.apply(rs.getLong("CATEGORY_ID")));
                    task.setProgress(rs.getInt("PROGRESS"));
                    task.setId(id);
                    task.setDoneTime(rs.getTimestamp("DONE_TIME").toLocalDateTime());
                    task.setStateOfTask(TaskState.valueOf(rs.getString("STATE")));
                    task.setSpentTime(rs.getInt("SPENT_TIME"));
                    if (rs.next()) {
                        throw new DataAccessException("Multiple tasks with id " + id + " found");
                    }
                    return task;
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load task with id " + id, ex);
        }
    }

    @Override
    public void update(Task entity) throws DataAccessException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Task has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE TASK SET " +
                             "NAME = ?, " +
                             "PLACE = ?, " +
                             "DEADLINE = ?, " +
                             "START_DATE = ?, " +
                             "HOURS_NEEDED = ?, " +
                             "CATEGORY_ID = ?, " +
                             "PROGRESS = ?, " +
                             "DONE_TIME = ?, " +
                             "STATE = ?, " +
                             "SPENT_TIME = ? " +
                             "WHERE ID = ?")) {
            st.setString(1, entity.getTaskName());
            st.setString(2, entity.getLocation());
            st.setTimestamp(3, Timestamp.valueOf(entity.getDeadline()));
            st.setTimestamp(4, Timestamp.valueOf(entity.getStartTime()));
            st.setInt(5, entity.getEstimatedTime());
            if (entity.getCategory() == null) {
                st.setNull(6, Types.BIGINT);
            } else {
                st.setLong(6, entity.getCategory().getId());
            }
            st.setInt(7, entity.getProgress());
            st.setTimestamp(8, Timestamp.valueOf(entity.getDoneTime()));
            st.setString(9, entity.getStateOfTask().name());
            st.setInt(10, entity.getSpentTime());
            st.setLong(11, entity.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing task: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update task " + entity, ex);
        }
    }

    @Override
    public void delete(Task entity) throws DataAccessException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Task has null ID: " + entity);
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM TASK WHERE ID = ?")) {
            st.setLong(1, entity.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing task: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete task " + entity, ex);
        }
    }

    boolean tableExists() {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, "APP", "TASK", null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + "APP" + "." + "TASK" + " exist", ex);
        }
    }

    void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.TASK (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "PLACE VARCHAR(100) NOT NULL, " +
                    "DEADLINE TIMESTAMP NOT NULL, " +
                    "START_DATE TIMESTAMP NOT NULL, " +
                    "HOURS_NEEDED SMALLINT NOT NULL, " +
                    "CATEGORY_ID BIGINT REFERENCES CATEGORY(ID), " +
                    "PROGRESS SMALLINT, " +
                    "DONE_TIME TIMESTAMP NOT NULL, " +
                    "STATE VARCHAR(15) NOT NULL CONSTRAINT STATE_CHECK CHECK (STATE IN ('WAITING','IN_PROGRESS','FINISHED')), " +
                    "SPENT_TIME SMALLINT" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create Task table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.TASK");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop TASK table", ex);
        }
    }
}