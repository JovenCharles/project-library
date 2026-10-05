/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Persists {@link LocalDate} values as ISO-8601 strings using ORMLite.
 */
public final class LocalDatePersister extends BaseDataType {

    /**
     * Singleton instance of the persister.
     */
    private static final LocalDatePersister SINGLETON = new LocalDatePersister();

    /**
     * Creates the LocalDate persister.
     */
    private LocalDatePersister() {
        super(SqlType.STRING, new Class<?>[]{LocalDate.class});
    }

    /**
     * Returns the singleton instance.
     *
     * @return the LocalDate persister instance
     */
    public static LocalDatePersister getSingleton() {
        return SINGLETON;
    }

    /**
     * Converts a default database value to its SQL representation.
     *
     * @param fieldType field metadata
     * @param defaultStr default value
     * @return the default value unchanged
     */
    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return defaultStr;
    }

    /**
     * Reads a LocalDate value from the database as a string.
     *
     * @param fieldType field metadata
     * @param results database result set
     * @param columnPos column position
     * @return the SQL string value
     * @throws SQLException if the database value cannot be read
     */
    @Override
    public Object resultToSqlArg(
            FieldType fieldType,
            DatabaseResults results,
            int columnPos
    ) throws SQLException {
        return results.getString(columnPos);
    }

    /**
     * Converts a SQL string into a LocalDate.
     *
     * @param fieldType field metadata
     * @param sqlArg SQL value
     * @param columnPos column position
     * @return the parsed LocalDate
     */
    @Override
    public Object sqlArgToJava(
            FieldType fieldType,
            Object sqlArg,
            int columnPos
    ) {
        return LocalDate.parse((String) sqlArg);
    }

    /**
     * Converts a LocalDate into its ISO-8601 string representation.
     *
     * @param fieldType field metadata
     * @param javaObject LocalDate value
     * @return the date as a string
     */
    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return javaObject.toString();
    }
}