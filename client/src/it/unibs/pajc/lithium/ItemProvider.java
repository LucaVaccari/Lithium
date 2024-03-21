package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.User;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

/**
 * Static class providing methods for querying the server.
 * Also implements caching. It is preferable to use this class instead of sending HTTP requests directly.
 */
public final class ItemProvider {
    // TODO: implement caching
    private static final HashMap<Class<?>, PriorityQueue<?>> cache = new HashMap<>();
    private static final Gson gson = new Gson();

    public static <T> T getItem(int id, Class<T> objType) {
        var json = HttpHandler.get("/%s/%d".formatted(objType.getSimpleName().toLowerCase(), id));
        return gson.fromJson(json, objType);
    }

    public static <T> T[] searchItem(int numberOfResults, String searchTerm, Class<T[]> arrType) {
        String tracksJson = HttpHandler.get("/%s/search?number-of-results=%d&search=%s".formatted(
                arrType.getComponentType().getSimpleName().toLowerCase(), numberOfResults, searchTerm));
        return gson.fromJson(tracksJson, arrType);
    }

    public static <T> T[] getItems(Integer[] ids, Class<T> objType) {
        T[] items = (T[]) Array.newInstance(objType, ids.length);
        IntStream.range(0, ids.length).forEach(i -> items[i] = getItem(ids[i], objType));
        return items;
    }

    public static User getPlaylistOwner(Playlist playlist) {
        var ownerJson = HttpHandler.get("/user/%d".formatted(playlist.getOwnerId()));
        return gson.fromJson(ownerJson, User.class);
    }

    public static Image getImage(String path) {
        return new Image(new ByteArrayInputStream(HttpHandler.getBase64Img(path)));
    }

    private ItemProvider() {
    }
}
