package it.unibs.pajc.db;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Table;

import java.io.Closeable;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Establishes a connection with the SQLie db and provides methods for interacting with it (independent of the rest of the program)
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

    // CREATE
    // TODO: create
    public <T> void createObject(T object) {
        // TODO: implement
    }

    // READ

    /**
     * Performs a query on the db using the annotated fields of the specified class.
     *
     * @param objType       The class which fields will be used in the SQL query
     * @param optionalQuery An optional part of the query appended to "select * from [table] ". Useful for where or join
     * @param fields        The list of column names from which to retrieve data from the db. If empty all the columns will be retrieved
     * @param <T>           The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getObjects(Class<T> objType, String optionalQuery, String[] fields) throws IllegalArgumentException {
        if (!objType.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException("The specified class must have the @Table annotation");
        try {
            var statement = connection.createStatement();

            String fieldList = fields.length == 0 ? "*" : String.join(", ", fields);
            String tableName = objType.getAnnotation(Table.class).name();
            String query = "SELECT " + fieldList + " from " + tableName + " " + optionalQuery;

            var resultSet = statement.executeQuery(query);
            var rsMetaData = resultSet.getMetaData();
            var objects = new ArrayList<T>();
            while (resultSet.next()) {
                T object = objType.getDeclaredConstructor().newInstance();
                for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
                    for (var field : objType.getDeclaredFields()) {
                        if (!field.isAnnotationPresent(Column.class)) continue;
                        field.setAccessible(true);
                        String columnName = field.getAnnotation(Column.class).name();
                        Object result = resultSet.getObject(columnName);
                        field.set(object, result);
                    }
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
     * @param optionalQuery An optional part of the query appended to "select * from [table] ". Useful for where or join
     * @param <T>           The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    public <T> T[] getObjects(Class<T> objType, String optionalQuery) throws IllegalArgumentException {
        return getObjects(objType, optionalQuery, new String[]{});
    }

    /**
     * Performs a query on the db using the annotated fields of the specified class.
     *
     * @param objType The class which fields will be used in the SQL query
     * @param <T>     The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    public <T> T[] getObjects(Class<T> objType) throws IllegalArgumentException {
        return getObjects(objType, "");
    }

    // UPDATE
    // TODO: update
    // DELETE
    // TODO: delete

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}