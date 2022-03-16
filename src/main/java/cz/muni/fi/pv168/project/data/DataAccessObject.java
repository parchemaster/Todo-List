package cz.muni.fi.pv168.project.data;

import java.util.Collection;

public interface DataAccessObject<E> {

    /**
     * Creates a new entity using the underlying data source
     * @param entity entity to be persisted
     * @throws IllegalArgumentException when the entity has already been persisted
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    void create(E entity) throws DataAccessException;

    /**
     * Reads all entities from the underlying data source
     * @return collection of all entities known to the underlying data source
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    Collection<E> findAll() throws DataAccessException;

    /**
     * Updates an entity using the underlying data source
     * @param entity entity to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    void update(E entity) throws DataAccessException;

    /**
     * Deletes an entity using the underlying data source
     * @param entity entity to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    void delete(E entity) throws DataAccessException;

}
