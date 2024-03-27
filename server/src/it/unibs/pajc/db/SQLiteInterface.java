package it.unibs.pajc.db;

import it.unibs.pajc.lithium.Logger;

import java.io.Closeable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Establishes a connection with the SQLie db and provides methods for interacting with it (independent of the rest
 * of the program)
 */
public class SQLiteInterface implements Closeable {
    private Connection connection;

    /**
     * Connects the interface to the db.
     * This method must be called before all others.
     *
     * @param dbUrl The URL of the db (online or local)
     */
    public void connect(String dbUrl) {
        try {
            connection = DriverManager.getConnection(dbUrl);
            Logger.log("Connection to db established");
        } catch (SQLException e) {
            Logger.logError(this, e);
        }
    }

    @Override
    public void close() {
        try {
            Logger.log("Connection to db interrupted");
            connection.close();
        } catch (SQLException e) {
            Logger.logError(this, e);
        }
    }

    // region CREATE
    public <T> void createObjects(T[] objects, Class<T> objType, boolean ignoreIds) {
        if (!objType.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException(objType.getName() + " does not have the @Table annotation");
        try {
            var statement = connection.createStatement();

            // BUILD QUERY
            Field[] declaredFields = objType.getDeclaredFields();
            var sqlBuilder = new StringBuilder();
            sqlBuilder.ensureCapacity(14 + 10 * objects.length * (1 + declaredFields.length));
            var tableName = objType.getAnnotation(Table.class).name();
            sqlBuilder.append("INSERT INTO ").append(tableName).append("(");

            var previousValueInserted = false;
            for (var column : declaredFields) {
                if (!column.isAnnotationPresent(Column.class) || (ignoreIds && column.isAnnotationPresent(Id.class))) {
                    previousValueInserted = false;
                    continue;
                }
                if (previousValueInserted) sqlBuilder.append(", ");
                sqlBuilder.append(column.getAnnotation(Column.class).name());
                previousValueInserted = true;
            }

            sqlBuilder.append(") VALUES");
            for (int i = 0; i < objects.length; i++) {
                if (i > 0) sqlBuilder.append(", ");
                sqlBuilder.append("(");
                previousValueInserted = false;
                for (var column : declaredFields) {
                    column.setAccessible(true);
                    if (!column.isAnnotationPresent(Column.class) ||
                            (ignoreIds && column.isAnnotationPresent(Id.class))) {
                        previousValueInserted = false;
                        continue;
                    }
                    if (previousValueInserted) sqlBuilder.append(", ");
                    Object valueToInsert = column.get(objects[i]);
                    if (valueToInsert != null) sqlBuilder.append("'").append(valueToInsert).append("'");
                    else sqlBuilder.append("NULL");
                    previousValueInserted = true;
                }
                sqlBuilder.append(")");
            }

            System.out.println(sqlBuilder);
            statement.executeUpdate(sqlBuilder.toString());

        } catch (Exception e) {
            Logger.logError(this, e);
        }
    }

    /**
     * Execute an insert sql query
     *
     * @param object    The object to insert. It must have the @Table annotation
     * @param objType   The type of the object to insert
     * @param ignoreIds Whether to consider ids assigned in the object or not.
     *                  Most of the time it should be true, unless a relationship row is being inserted.
     * @param <T>       The type of the object to insert
     */
    public <T> void createObject(T object, Class<T> objType, boolean ignoreIds) {
        var array = (T[]) Array.newInstance(objType, 1);
        array[0] = object;
        createObjects(array, objType, ignoreIds);
    }

    // endregion

    //region READ
    private ResultSet genericGetQuery(String[] fields, String tableName, String optionalQuery) throws SQLException {
        var statement = connection.createStatement();
        String fieldList = fields.length == 0 ? "*" : String.join(", ", fields);
        String query = "SELECT %s from %s %s".formatted(fieldList, tableName, optionalQuery);
        return statement.executeQuery(query);
    }

    /**
     * Performs a query on the db using the annotated fields of the specified class.
     *
     * @param objType       The class which fields will be used in the SQL query
     * @param maxSize       The max number of results that can be fetched from the db
     * @param optionalQuery An optional part of the query appended to "select * from [table] ". Useful for where or join
     * @param fields        The list of column names from which to retrieve data from the db.
     *                      If empty, all the columns will be retrieved
     * @param <T>           The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    public <T> T[] getObjects(Class<T> objType, int maxSize, String optionalQuery, String... fields)
            throws IllegalArgumentException {
        if (!objType.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException(objType.getName() + " does not have the @Table annotation");
        try {
            String tableName = objType.getAnnotation(Table.class).name();
            var resultSet = genericGetQuery(fields, tableName, optionalQuery);
            var rsMetaData = resultSet.getMetaData();
            var objects = new ArrayList<T>();
            while (resultSet.next() && objects.size() < maxSize) {
                T object = objType.getDeclaredConstructor().newInstance();
                Field id = null;
                for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
                    for (var field : objType.getDeclaredFields()) {
                        if (!field.isAnnotationPresent(Column.class)) continue;
                        field.setAccessible(true);
                        String columnName = field.getAnnotation(Column.class).name();
                        Object result = resultSet.getObject(columnName);
                        field.set(object, result);

                        if (field.isAnnotationPresent(Id.class)) id = field;
                    }
                }
                if (id != null) {
                    Field finalId = id;
                    Arrays.stream(objType.getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(ForeignKey.class)).forEach(field -> {
                                try {
                                    var mtmAnnotation = field.getAnnotation(ForeignKey.class);
                                    finalId.setAccessible(true);
                                    field.setAccessible(true);
                                    String otherTableColumnName = mtmAnnotation.otherTableColumnName();
                                    String columnName = finalId.getAnnotation(Column.class).name();
                                    int objectId = (int) finalId.get(object);
                                    var mtmResultSet =
                                            genericGetQuery(new String[]{otherTableColumnName},
                                                    mtmAnnotation.otherTableName(),
                                                    "where %s = %d".formatted(columnName, objectId));
                                    var ids = new ArrayList<Integer>();
                                    while (mtmResultSet.next()) {
                                        ids.add(mtmResultSet.getInt(otherTableColumnName));
                                    }
                                    field.set(object, ids.toArray(new Integer[0]));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                }
                objects.add(object);
            }
            return objects.toArray((T[]) Array.newInstance(objType, objects.size()));
        } catch (Exception e) {
            Logger.logError(this, e);
            return (T[]) Array.newInstance(objType, 0);
        }
    }

    /**
     * Performs a query on the db using the annotated fields of the specified class.
     *
     * @param objType The class which fields will be used in the SQL query
     * @param maxSize The max number of results that can be fetched from the db
     * @param <T>     The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    public <T> T[] getObjects(Class<T> objType, int maxSize) throws IllegalArgumentException {
        return getObjects(objType, maxSize, "");
    }

    public int getNumberOfEntries(String tableName, String optionalQuery) {
        try {
            var resultSet = genericGetQuery(new String[]{"COUNT(*) AS n"}, tableName, optionalQuery);
            return resultSet.getInt("n");
        } catch (SQLException e) {
            Logger.logError(this, e);
            return -1;
        }
    }

    //endregion
    // UPDATE
    public <T> void updateObject(Map<String, String> updates, Class<T> objType, String appendedQuery) {
        if (!objType.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException(objType.getName() + " does not have the @Table annotation");

        try {
            var statement = connection.createStatement();

            // BUILD QUERY
            var sqlBuilder = new StringBuilder();
            sqlBuilder.ensureCapacity(12 + 15 * (1 + updates.size()));
            var tableName = objType.getAnnotation(Table.class).name();
            sqlBuilder.append("UPDATE ").append(tableName).append(" SET ");

            var previousValueInserted = false;
            for (var key : updates.keySet()) {
                if (previousValueInserted) sqlBuilder.append(", ");
                sqlBuilder.append(key).append(" = '").append(updates.get(key)).append("'");
                previousValueInserted = true;
            }
            sqlBuilder.append(" ").append(appendedQuery);

            statement.executeUpdate(sqlBuilder.toString());
        } catch (Exception e) {
            Logger.logError(this, e);
        }
    }
    // DELETE

    public <T> void deleteObject(T object, Class<T> objType) {
        if (!objType.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException("The argument class must have the @Table annotation");

        try {
            var statement = connection.createStatement();

            // BUILD QUERY
            Field[] declaredFields = objType.getDeclaredFields();
            var sqlBuilder = new StringBuilder();
            sqlBuilder.ensureCapacity(14 + 10 * (1 + declaredFields.length));
            var tableName = objType.getAnnotation(Table.class).name();
            sqlBuilder.append("DELETE FROM ").append(tableName).append(" WHERE ");

            var previousValueInserted = false;
            for (var column : Arrays.stream(declaredFields).filter(f -> f.isAnnotationPresent(Id.class))
                    .toArray(Field[]::new)) {
                if (!column.isAnnotationPresent(Column.class)) {
                    previousValueInserted = false;
                    continue;
                }
                if (previousValueInserted) sqlBuilder.append(" and ");
                sqlBuilder.append(column.getAnnotation(Column.class).name());
                sqlBuilder.append(" = ");
                column.setAccessible(true);
                sqlBuilder.append(column.get(object));
                previousValueInserted = true;
            }

            statement.executeUpdate(sqlBuilder.toString());
        } catch (Exception e) {
            Logger.logError(this, e);
        }
    }
}