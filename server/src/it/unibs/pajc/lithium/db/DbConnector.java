package it.unibs.pajc.lithium.db;

import it.unibs.pajc.db.SQLiteInterface;
import it.unibs.pajc.lithium.db.om.Album;

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

    // Albums, artists, genres, playlists, tracks, users
    // region CREATE
    public void createAlbum(Album album) {
        dbInf.createObject(album, Album.class);

        // TODO insert artist and genre list
    }

    // endregion
    // READ
    public Album[] getAllAlbums() {
        return dbInf.getObjects(Album.class);

        // TODO: get artists and genres
    }

    public Album getAlbumById(int id) {
        String queryFilter = "where album_id = " + id;
        Album[] albums = dbInf.getObjects(Album.class, queryFilter);
        if (albums.length == 1) return albums[0];
        else if (albums.length == 0) return null;
        else throw new RuntimeException("There's more than one album with the same id");

        // TODO: get artists and genres
    }
    // UPDATE
    // TODO: update
    // DELETE
    // TODO: delete
}
