package it.unibs.pajc.lithium.db;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.SQLiteInterface;
import it.unibs.pajc.db.Table;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.db.om.UserSavedPlaylist;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Map;

/**
 * Provides CRUD methods for managing data in the Lithium database (albums, artists, tracks, etc...)
 * Uses {@link it.unibs.pajc.db.SQLiteInterface} for interacting with the db
 */
public class DbConnector implements Closeable {
    private SQLiteInterface dbInf;

    @Override
    public void close() {
        dbInf.close();
    }

    /**
     * Connects the interface to the db.
     * This method must be called before all others.
     *
     * @param dbUrl The URL of the db (online or local)
     */
    public void connect(String dbUrl) {
        dbInf = new SQLiteInterface();
        dbInf.connect(dbUrl);
    }

    //region CREATE
    public void registerUser(String name, String pswHash) throws IllegalArgumentException {
        if (getUserByName(name) != null) throw new IllegalArgumentException(
                "The user %s already exists, you cannot register it again".formatted(name));

        User user = new User(name, pswHash);
        dbInf.createObject(user, User.class, true);
    }

    public int createPlaylist(Playlist playlist) {
        createObject(playlist, Playlist.class, true);
        String whereFilter =
                ("where playlist_title = '%s' and playlist_description = '%s' and user_id = '%s' and cover_img_path =" +
                        " '%s'").formatted(playlist.getName(), playlist.getDescription(), playlist.getOwnerId(),
                        playlist.getImgPath());
        var createdPlaylist = dbInf.getObjects(Playlist.class, 1, whereFilter)[0];
        createObject(new UserSavedPlaylist(createdPlaylist.getOwnerId(), createdPlaylist.getId()),
                UserSavedPlaylist.class, false);
        return createdPlaylist.getId();
    }

    public <T> void createObject(T item, Class<T> objType, boolean ignoreIds) {
        dbInf.createObject(item, objType, ignoreIds);
    }

    //endregion
    //region READ

    public User getUserByName(String name) {
        String queryFilter = "where username = '%s'".formatted(name);
        User[] users = dbInf.getObjects(User.class, 5, queryFilter);
        if (users.length == 1) return users[0];
        else if (users.length == 0) return null;
        else throw new RuntimeException("There's more than one user with the same name");
    }

    /**
     * Authenticate the user
     *
     * @param name    The name of the user
     * @param pswHash The hash of the user password
     * @return The id of the user authenticated or -1 if it is not authenticated
     */
    public int authenticateUser(String name, String pswHash) {
        String queryFilter = "where username = '%s' and pwd_hash = '%s'".formatted(name, pswHash);
        User[] users = dbInf.getObjects(User.class, 5, queryFilter);
        if (users.length == 1) return users[0].getId();
        else if (users.length == 0) return -1;
        else throw new RuntimeException("There's more than one user with the same name");
    }

    public <T> T[] getObjects(int numberOfResults, Class<T> objType) {
        return dbInf.getObjects(objType, numberOfResults);
    }

    public <T> T[] searchObjects(int numberOfResults, Class<T> objType, String columnName, String searchTerm) {
        String queryFilter = "where %s LIKE '%%%s%%'".formatted(columnName, searchTerm);
        return dbInf.getObjects(objType, numberOfResults, queryFilter);
    }

    /**
     * Retrieves an object from the db by searching for its id
     *
     * @param id      The id of the object to search
     * @param objType The type class of the object to search for. It must have the {@link Table} annotation and at least
     *                one field with the {@link Id} annotation (only the first annotated field is considered}
     * @param <T>     The type of the object to search
     * @return The object found or null if it is not found
     */
    public <T> T getObjectById(int id, Class<T> objType) {
        var idName = getIdName(objType);
        if (idName == null) return null;
        String queryFilter = "where %s = '%d'".formatted(idName, id);
        T[] objects = dbInf.getObjects(objType, 5, queryFilter);
        if (objects.length == 1) return objects[0];
        else if (objects.length == 0) return null;
        else throw new RuntimeException(
                    "There's more than one %s with the same id".formatted(objType.getName().toLowerCase()));
    }

    public int getNumberOfPlaylistsWithName(String name) {
        var whereQuery = "where playlist_title LIKE '%%%s%%'".formatted(name);
        return dbInf.getNumberOfEntries("playlist", whereQuery);
    }

    public int getNumberOfSavesPerTrack(int trackId) {
        var whereQuery = "where trackId = %d".formatted(trackId);
        return dbInf.getNumberOfEntries("track_in_playlist", whereQuery);
    }

    //endregion
    //region UPDATE
    public <T> void updateItem(int id, Map<String, String> updates, Class<T> objType) {
        var idName = getIdName(objType);
        if (idName == null) throw new IllegalArgumentException("Invalid class provided");
        dbInf.updateObject(updates, objType, "where %s = %d".formatted(idName, id));
    }

    //endregion
    //region DELETE
    public <T> void deleteObject(T item, Class<T> objType) {
        dbInf.deleteObject(item, objType);
    }
    //endregion

    private <T> String getIdName(Class<T> objType) {
        var objFields = objType.getDeclaredFields();
        var idField = Arrays.stream(objFields).filter(f -> f.isAnnotationPresent(Id.class)).findFirst();
        if (idField.isEmpty()) return null;
        if (!idField.get().isAnnotationPresent(Column.class)) return null;

        return idField.get().getAnnotation(Column.class).name();
    }
}
