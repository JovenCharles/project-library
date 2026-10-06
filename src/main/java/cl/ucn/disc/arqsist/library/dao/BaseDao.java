/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Base DAO with common CRUD and transaction operations.
 *
 * @param <T> entity type managed by the DAO
 */
public class BaseDao<T> {

    /**
     * ORMLite DAO used for persistence operations.
     */
    protected final Dao<T, Integer> dao;

    /**
     * Creates a base DAO for the given entity type.
     *
     * @param connectionSource database connection source
     * @param clazz entity class
     */
    public BaseDao(ConnectionSource connectionSource, Class<T> clazz) {
        try {
            this.dao = DaoManager.createDao(connectionSource, clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all persisted entities.
     *
     * @return all entities
     */
    public List<T> findAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id entity identifier
     * @return entity or null if it does not exist
     */
    public T findById(int id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Persists an entity.
     *
     * @param entity entity to create
     */
    public void create(T entity) {
        try {
            dao.create(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an entity.
     *
     * @param entity entity to update
     */
    public void update(T entity) {
        try {
            dao.update(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes an entity.
     *
     * @param entity entity to delete
     */
    public void delete(T entity) {
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes an operation inside a database transaction.
     *
     * @param callable operation to execute
     * @param <R> result type
     * @return transaction result
     * @throws SQLException if the transaction cannot be completed
     */
    public <R> R transaction(Callable<R> callable) throws SQLException {
        try {
            return TransactionManager.callInTransaction(
                    dao.getConnectionSource(),
                    callable
            );
        } catch (SQLException e) {
            if (e.getCause() instanceof RuntimeException cause) {
                throw cause;
            }
            throw e;
        }
    }
}