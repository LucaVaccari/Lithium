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
    public void registerUser(String name, String pswHash) {
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
        User[] users = dbInf.getObjects(User.class, queryFilter);
        if (users.length == 1) return users[0];
        else if (users.length == 0) return null;
        else throw new RuntimeException("There's more than one user with the same name");
    }

    public boolean authenticateUser(String name, String pswHash) {
        String queryFilter = "where username = '%s' and pwd_hash = '%s'".formatted(name, pswHash);
        User[] users = dbInf.getObjects(User.class, queryFilter);
        if (users.length == 1) return true;
        else if (users.length == 0) return false;
        else throw new RuntimeException("There's more than one user with the same name");
    }

    public Album[] getAllAlbums() {
        return dbInf.getObjects(Album.class);

        // TODO: get artists and genres
    }

    public Album getAlbumById(int id) {
        String queryFilter = "where album_id = '%d'".formatted(id);
        Album[] albums = dbInf.getObjects(Album.class, queryFilter);
        if (albums.length == 1) return albums[0];
        else if (albums.length == 0) return null;
        else throw new RuntimeException("There's more than one album with the same id");

        // TODO: get artists and genres
    }
    //endregion
    //region UPDATE
    //endregion
    //region DELETE
    //endregion
}