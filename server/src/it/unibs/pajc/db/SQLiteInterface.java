package it.unibs.pajc.db;

import java.io.Closeable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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
            System.out.println("Connection to db established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            System.out.println("Connection to db interrupted");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // region CREATE
    // TODO: create

    public <T> void createObjects(T[] objects, Class<T> objType) {
        if (!objType.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException("The argument class must have the @Table annotation");
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
                if (!column.isAnnotationPresent(Column.class) || column.isAnnotationPresent(Id.class)) {
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
                    if (!column.isAnnotationPresent(Column.class) || column.isAnnotationPresent(Id.class)) {
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
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void createObject(T object, Class<T> objType) {
        var array = (T[]) Array.newInstance(objType, 1);
        array[0] = object;
        createObjects(array, objType);
    }

    // endregion

    //region READ

    /**
     * Performs a query on the db using the annotated fields of the specified class.
     *
     * @param objType       The class which fields will be used in the SQL query
     * @param maxSize       The max number of results that can be fetched from the db
     * @param optionalQuery An optional part of the query appended to "select * from [table] ". Useful for where or join
     * @param fields        The list of column names from which to retrieve data from the db. If empty all the
     *                      columns will be retrieved
     * @param <T>           The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getObjects(Class<T> objType, int maxSize, String optionalQuery, String[] fields)
            throws IllegalArgumentException {
        if (!objType.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException("The specified class must have the @Table annotation");
        try {
            var statement = connection.createStatement();

            String fieldList = fields.length == 0 ? "*" : String.join(", ", fields);
            String tableName = objType.getAnnotation(Table.class).name();
            String query = "SELECT %s from %s %s".formatted(fieldList, tableName, optionalQuery);

            var resultSet = statement.executeQuery(query);
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

                        if (field.isAnnotationPresent(Id.class))
                            id = field;
                    }
                }
                if (id != null) {
                    Field finalId = id;
                    Arrays.stream(objType.getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(ManyToMany.class)).forEach(field -> {
                                try {
                                    var mtmAnnotation = field.getAnnotation(ManyToMany.class);
                                    finalId.setAccessible(true);
                                    field.setAccessible(true);
                                    String otherTableColumnName = mtmAnnotation.otherTableColumnName();
                                    var mtmQuery =
                                            "SELECT %s from %s where %s = %d".formatted(otherTableColumnName,
                                                    mtmAnnotation.otherTableName(),
                                                    finalId.getAnnotation(Column.class).name(),
                                                    (int) finalId.get(object));
                                    var mtmResultSet = statement.executeQuery(mtmQuery);
                                    var ids = new ArrayList<Integer>();
                                    while (mtmResultSet.next()) {
                                        ids.add(resultSet.getInt(otherTableColumnName));
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
            e.printStackTrace();
            return (T[]) Array.newInstance(objType, 0);
        }
    }

    /**
     * Performs a query on the db using the annotated fields of the specified class.
     *
     * @param objType       The class which fields will be used in the SQL query
     * @param maxSize       The max number of results that can be fetched from the db
     * @param optionalQuery An optional part of the query appended to "select * from [table] ". Useful for where or join
     * @param <T>           The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    public <T> T[] getObjects(Class<T> objType, int maxSize, String optionalQuery) throws IllegalArgumentException {
        return getObjects(objType, maxSize, optionalQuery, new String[]{});
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
    //endregion
    // UPDATE
    // TODO: update
    // DELETE

    // TODO: delete
}