package cz.muni.fi.pv168.project.data;

import cz.muni.fi.pv168.project.model.Subtask;
import cz.muni.fi.pv168.project.model.Task;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SubtaskDao implements DataAccessObject<Subtask> {

    private final DataSource dataSource;
    private final Function<Long, Task> taskResolver;

    public SubtaskDao(DataSource dataSource, Function<Long, Task> taskResolver) {
        this.dataSource = dataSource;
        this.taskResolver = taskResolver;
    }

    @Override
    public void create(Subtask entity) throws DataAccessException {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("Subtask already has ID: " + entity);
        }
        if (entity.getTask() == null) {
            throw new IllegalArgumentException("Subtask should have a parent task.");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO SUBTASK(NAME, DONE, TASK_ID) VALUES (?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, entity.getName());
            st.setBoolean(2, entity.isDone());
            if (entity.getTask() == null) {
                st.setNull(3, Types.BIGINT);
            } else {
                st.setLong(3, entity.getTask().getId());
            }
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() != 1) {
                    throw new DataAccessException("Failed to fetch generated key: compound key returned for subtask: " + entity);
                }
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for subtask: " + entity);
                }
                if (rs.next()) {
                    throw new DataAccessException("Failed to fetch generated key: multiple keys returned for subtask: " + entity);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store subtask " + entity, ex);
        }
    }

    @Override
    public Collection<Subtask> findAll() throws DataAccessException {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, DONE, TASK_ID FROM SUBTASK")) {

            List<Subtask> subtasks = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var subtask = new Subtask(
                            rs.getString("NAME"),
                            rs.getBoolean("DONE"),
                            taskResolver.apply(rs.getLong("TASK_ID")));
                    subtask.setId(rs.getLong("ID"));
                    subtasks.add(subtask);
                }
            }
            return subtasks;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all subtasks", ex);
        }
    }

    public List<Subtask> findByTaskId(Long taskId) throws DataAccessException {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, DONE FROM SUBTASK WHERE TASK_ID = ?")) {
             st.setLong(1, taskId);

            List<Subtask> subtasks = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var subtask = new Subtask(
                            rs.getString("NAME"),
                            rs.getBoolean("DONE"),
                            taskResolver.apply(taskId));
                    subtask.setId(rs.getLong("ID"));
                    subtasks.add(subtask);
                }
            }
            return subtasks;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load subtasks by task id ", ex);
        }
    }

    @Override
    public void update(Subtask entity) throws DataAccessException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Subtask has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE SUBTASK SET NAME = ?, DONE = ?, TASK_ID = ? WHERE ID = ?")) {
            st.setString(1, entity.getName());
            st.setBoolean(2, entity.isDone());
            if (entity.getTask() == null) {
                st.setNull(3, Types.BIGINT);
            } else {
                st.setLong(3, entity.getTask().getId());
            }
            st.setLong(4, entity.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing subtask: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update subtask " + entity, ex);
        }
    }

    @Override
    public void delete(Subtask entity) throws DataAccessException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Subtask has null ID: " + entity);
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM SUBTASK WHERE ID = ?")) {
            st.setLong(1, entity.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing subtask: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete subtask " + entity, ex);
        }
    }

    boolean tableExists() {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, "APP", "SUBTASK", null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + "APP" + "." + "SUBTASK" + " exist", ex);
        }
    }

    void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.SUBTASK (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "DONE BOOLEAN NOT NULL DEFAULT FALSE, " +
                    "TASK_ID BIGINT REFERENCES TASK(ID) ON DELETE CASCADE NOT NULL" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create Subtask table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.SUBTASK");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop SUBTASK table", ex);
        }
    }
}
