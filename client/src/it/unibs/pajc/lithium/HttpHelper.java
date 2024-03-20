package it.unibs.pajc.lithium;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.db.om.*;

/**
 * Static class providing useful methods for interacting with the Lithium server
 */
public final class HttpHelper {
    public static Artist[] getArtists(Integer[] ids) {
        var artists = new Artist[ids.length];
        for (int i = 0; i < ids.length; i++) {
            var id = ids[i];
            var json = HttpHandler.get("/artist/%d".formatted(id));
            artists[i] = ClientMain.getGson().fromJson(json, Artist.class);
        }
        return artists;
    }

    public static User getPlaylistOwner(Playlist playlist) {
        var ownerJson = HttpHandler.get("/user/%d".formatted(playlist.getOwnerId()));
        return ClientMain.getGson().fromJson(ownerJson, User.class);
    }

    private HttpHelper() {
    }
}
