package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import it.unibs.pajc.HttpHandler;
import javafx.scene.image.Image;

import java.lang.reflect.Array;
import java.util.stream.IntStream;

/**
 * Static class providing methods for querying the server.
 * Also implements caching. It is preferable to use this class instead of sending HTTP requests directly.
 */
public final class ItemProvider {
    // TODO: implement caching with Guava
    private static final Gson gson = new Gson();

    public static <T> T getItem(int id, Class<T> objType) {
        var json = HttpHandler.get("%s?id=%d".formatted(objType.getSimpleName().toLowerCase(), id));
        return gson.fromJson(json, objType);
    }

    public static <T> T[] searchItem(int numberOfResults, String searchTerm, Class<T[]> arrType, String fieldName) {
        String json = HttpHandler.get("%s?number-of-results=%d&field=%s&search=%s".formatted(
                arrType.getComponentType().getSimpleName().toLowerCase(), numberOfResults, fieldName, searchTerm));
        T[] items = gson.fromJson(json, arrType);
        // TODO cache items
        return items;
    }

    public static <T> T[] getItems(Integer[] ids, Class<T> objType) {
        T[] items = (T[]) Array.newInstance(objType, ids.length);
        IntStream.range(0, ids.length).forEach(i -> items[i] = getItem(ids[i], objType));
        return items;
    }

    public static Image getImage(String path) {
        return new Image(HttpHandler.buildUrl(path));
    }

    private ItemProvider() {
    }
}
