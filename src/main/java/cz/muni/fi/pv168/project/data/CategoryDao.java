package cz.muni.fi.pv168.project.data;

import cz.muni.fi.pv168.project.model.Category;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryDao implements DataAccessObject<Category>{

    private final DataSource dataSource;

    public CategoryDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void create(Category entity) throws DataAccessException {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("Category already has ID: " + entity);
        }
        if (findByName(entity.getName()) != null) {
            throw new DataAccessException("Category with name " + entity.getName() + " already exists.");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO CATEGORY (NAME) VALUES (?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, entity.getName());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() != 1) {
                    throw new DataAccessException("Failed to fetch generated key: compound key returned for category: " + entity);
                }
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for category: " + entity);
                }
                if (rs.next()) {
                    throw new DataAccessException("Failed to fetch generated key: multiple keys returned for category: " + entity);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store category " + entity, ex);
        }
    }

    @Override
    public Collection<Category> findAll() throws DataAccessException {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME FROM CATEGORY")) {

            List<Category> categories = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var category = new Category(
                            rs.getString("NAME"));
                    category.setId(rs.getLong("ID"));
                    categories.add(category);
                }
            }
            return categories;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all categories", ex);
        }
    }

    public Category findById(Long id) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME FROM CATEGORY WHERE ID = ?")) {
            st.setLong(1, id);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var category = new Category(
                            rs.getString("NAME"));
                    category.setId(rs.getLong("ID"));
                    if (rs.next()) {
                        throw new DataAccessException("Multiple categories with id " + id + " found");
                    }
                    return category;
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load category with id " + id, ex);
        }
    }

    public Category findByName(String name) throws DataAccessException {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME FROM CATEGORY WHERE NAME = ?")) {
            st.setString(1, name);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var category = new Category(
                            rs.getString("NAME"));
                    category.setId(rs.getLong("ID"));
                    if (rs.next()) {
                        throw new DataAccessException("Multiple categories with name " + name + " found");
                    }
                    return category;
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load category with name " + name, ex);
        }
    }


    @Override
    public void update(Category entity) throws DataAccessException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Category has null ID");
        }
        if (findByName(entity.getName()) != null) {
            throw new DataAccessException("Category with name " + entity.getName() + " already exists.");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE CATEGORY SET NAME = ? WHERE ID = ?")) {
            st.setString(1, entity.getName());
            st.setLong(2, entity.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing category: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update category " + entity, ex);
        }
    }

    @Override
    public void delete(Category entity) throws DataAccessException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Category has null ID: " + entity);
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM CATEGORY WHERE ID = ?")) {
            st.setLong(1, entity.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing category: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete category " + entity, ex);
        }
    }

    boolean tableExists() {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, "APP", "CATEGORY", null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + "APP" + "." + "CATEGORY" + " exist", ex);
        }
    }

    void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {
            st.executeUpdate("CREATE TABLE APP.CATEGORY (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create CATEGORY table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE APP.CATEGORY");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop CATEGORY table", ex);
        }
    }
}
