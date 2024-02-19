package it.unibs.pajc.lithium.db;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Table;

import java.io.Closeable;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLiteInterface extends DBInterface implements Closeable {
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

    /**
     * Performs a query on the db using the annotated fields of the specified class.
     *
     * @param objClass The class which fields will be used in the SQL query
     * @param <T>      The type of the object to retrieve
     * @return An array of retrieved objects
     * @throws IllegalArgumentException Thrown when objClass does not have the {@link Table} annotation
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getObjects(Class<T> objClass) throws IllegalArgumentException {
        if (!objClass.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException("The specified class must have the @Table annotation");
        try {
            var statement = connection.createStatement();
            var query = "SELECT * from " + objClass.getAnnotation(Table.class).name();
            var resultSet = statement.executeQuery(query);
            var rsMetaData = resultSet.getMetaData();
            var objects = new ArrayList<T>();
            while (resultSet.next()) {
                T object = objClass.getDeclaredConstructor().newInstance();
                for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
                    for (var field : objClass.getDeclaredFields()) {
                        field.setAccessible(true);
                        String columnName = field.getAnnotation(Column.class).name();
                        Object result = resultSet.getObject(columnName);
                        field.set(object, result);
                    }
                }
                objects.add(object);
            }
            return objects.toArray((T[]) Array.newInstance(objClass, objects.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return (T[]) Array.newInstance(objClass, 0);
        }
    }

    @Override
    public boolean authenticateUser(String username, String passwordHash) {
        // TODO: implement
        return false;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}