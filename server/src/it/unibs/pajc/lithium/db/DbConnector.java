package it.unibs.pajc.lithium.db;

import it.unibs.pajc.db.SQLiteInterface;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.User;

import java.io.Closeable;

/**
 * Provides CRUD methods for managing data in the Lithium database (albums, artists, tracks, etc... )
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
        dbInf.createObject(user, User.class);
    }

    public void createAlbum(Album album) {
        dbInf.createObject(album, Album.class);

        // TODO insert artist and genre list
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

    public boolean authenticateUser(String name, String pswHash) {
        String queryFilter = "where username = '%s' and pwd_hash = '%s'".formatted(name, pswHash);
        User[] users = dbInf.getObjects(User.class, 5, queryFilter);
        if (users.length == 1) return true;
        else if (users.length == 0) return false;
        else throw new RuntimeException("There's more than one user with the same name");
    }

    public <T> T[] getObjects(int numberOfResults, Class<T> objType) {
        return dbInf.getObjects(objType, numberOfResults);
    }

    public <T> T getObjectById(int id, Class<T> objType, String idName) {
        String queryFilter = "where %s = '%d'".formatted(idName, id);
        T[] objects = dbInf.getObjects(objType, 5, queryFilter);
        if (objects.length == 1) return objects[0];
        else if (objects.length == 0) return null;
        else throw new RuntimeException(
                    "There's more than one %s with the same id".formatted(objType.getName().toLowerCase()));
    }

    public int getNumberOfSavesPerTrack(int trackId) {
        var whereQuery = "where trackId = %d".formatted(trackId);
        return dbInf.getNumberOfEntries("track_in_playlist", whereQuery);
    }
    //endregion
    //region UPDATE
    //endregion
    //region DELETE
    //endregion
}
