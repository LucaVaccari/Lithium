package it.unibs.pajc.lithium;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.db.om.*;
import it.unibs.pajc.lithium.gui.AlertUtil;
import javafx.scene.image.Image;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Static class providing methods for querying the server.
 * Also implements caching. It is preferable to use this class instead of sending HTTP requests directly.
 */
public final class ItemProvider {
    private static final Gson gson = new Gson();
    private static final HashMap<Class<? extends Item>, Cache<Integer, Item>> itemCaches;
    private static final Cache<String, Image> imgCache;

    static {
        itemCaches = new HashMap<>();
        itemCaches.put(Track.class, CacheBuilder.newBuilder().maximumSize(20).build());
        itemCaches.put(Album.class, CacheBuilder.newBuilder().maximumSize(20).build());
        itemCaches.put(Artist.class, CacheBuilder.newBuilder().maximumSize(20).build());
        itemCaches.put(Playlist.class, CacheBuilder.newBuilder().maximumSize(20).build());
        itemCaches.put(User.class, CacheBuilder.newBuilder().maximumSize(20).build());
        itemCaches.put(Genre.class, CacheBuilder.newBuilder().maximumSize(20).build());
        imgCache = CacheBuilder.newBuilder().maximumSize(30).build();
    }

    public static <T extends Item> T getItem(int id, Class<T> objType, boolean ignoreCache) {
        var itemCache = itemCaches.get(objType);
        if (!ignoreCache) {
            T cached = (T) itemCache.getIfPresent(id);
            if (cached != null) return cached;
        }

        try {
            var json = HttpHandler.get("%s?id=%d".formatted(objType.getSimpleName().toLowerCase(), id));
            T item = gson.fromJson(json, objType);
            itemCache.put(id, item);
            return item;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T extends Item> T getItem(int id, Class<T> objType) {
        return getItem(id, objType, false);
    }

    public static <T extends Item> T[] searchItem(int numberOfResults, String searchTerm, Class<T[]> arrType,
                                                  String fieldName) {
        Class<?> objType = arrType.getComponentType();
        var json = HttpHandler.get(
                "%s?number-of-results=%d&field=%s&search=%s".formatted(objType.getSimpleName().toLowerCase(),
                        numberOfResults, fieldName, searchTerm));
        try {
            return gson.fromJson(json, arrType);
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error while converting json", json);
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Item> T[] getItems(Integer[] ids, Class<T> objType) {
        T[] items = (T[]) Array.newInstance(objType, ids.length);
        IntStream.range(0, ids.length).forEach(i -> items[i] = getItem(ids[i], objType));
        return items;
    }

    public static Image getImage(String path) {
        Image cached = imgCache.getIfPresent(path);
        Image image = new Image(HttpHandler.buildUrl(path), true);
        if (cached == null) {
            imgCache.put(path, image);
            return image;
        } else return cached;
    }

    public static void saveItem(Integer id, Class<? extends Item> objType, boolean save) {
        var className = objType.getSimpleName().toLowerCase();
        var query = "?userId=%d&%sId=%d".formatted(AccountManager.getUser().getId(), className, id);
        var subURL = "user/save/" + className + query;
        System.out.println(save ? HttpHandler.post(subURL) : HttpHandler.delete(subURL));
    }

    public static void updateItem(Integer id, Class<? extends Item> objType, String... updates) {
        var query = "?id=%d&%s".formatted(id, String.join("&", updates));
        var subUrl = objType.getSimpleName().toLowerCase() + query;
        System.out.println(HttpHandler.put(subUrl));
    }

    public static int createPlaylist() {
        try {
            return Integer.parseInt(HttpHandler.post("/playlist?userId=" + AccountManager.getUser().getId()));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void deletePlaylist(int playlistId) {
        itemCaches.get(Playlist.class).invalidate(playlistId);
        System.out.println(HttpHandler.delete("/playlist?id=" + playlistId));
    }

    public static void addTrackToPlaylist(Playlist playlist, int trackId) {
        if (Arrays.asList(playlist.getTracksIds()).contains(trackId)) return;
        var subUrl = "playlist/add?id=%d&trackId=%d".formatted(playlist.getId(), trackId);
        System.out.println(HttpHandler.post(subUrl));
    }

    public static void removeTrackFromPlaylist(int playlistId, int trackId) {
        var subUrl = "playlist/add?id=%d&trackId=%d".formatted(playlistId, trackId);
        System.out.println(HttpHandler.delete(subUrl));
    }

    public static String getArtistNamesFormatted(Integer[] ids) {
        var artists = getItems(ids, Artist.class);
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        return String.join(", ", artistNames);
    }

    public static String getGenresFormatted(Integer[] ids) {
        var genres = new HashSet<>(List.of(ids));
        var genreNames = genres.stream().map(id -> getItem(id, Genre.class).getName()).toArray(String[]::new);
        return String.join(", ", genreNames);
    }

    private ItemProvider() {
    }
}
