package it.unibs.pajc.lithium.http;

import com.sun.net.httpserver.HttpExchange;
import it.unibs.pajc.db.Column;
import it.unibs.pajc.lithium.db.DbConnector;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.TrackInPlaylist;
import it.unibs.pajc.lithium.db.om.UserSavedPlaylist;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import static it.unibs.pajc.lithium.http.HttpHelper.*;
import static it.unibs.pajc.lithium.ServerMain.getDbConnector;
import static it.unibs.pajc.lithium.ServerMain.getGson;

/**
 * Provides methods which take parameters from HTTP requests and call {@link DbConnector} methods
 */
public class HttpRoutes {
    //region user authentication
    public static void userExists(HttpExchange exchange) throws IOException {
        var username = HttpHelper.queryParam(exchange, "username");
        if (!username.matches("^[a-zA-Z0-9_]+")) {
            sendStringResponse(exchange, 400,
                    "The username specified is not valid (it should contain only numbers, letters and/or _");
        }
        var body = getDbConnector().getUserByName(username) != null;
        sendStringResponse(exchange, 200, String.valueOf(body));
    }

    public static void authenticateUser(HttpExchange exchange) throws IOException {
        byte[] requestBody = exchange.getRequestBody().readAllBytes();
        String[] strings = new String(requestBody).split(",");
        validateUsername(exchange, strings);
        var auth = getDbConnector().authenticateUser(strings[0], strings[1]);
        sendStringResponse(exchange, 200, String.valueOf(auth));
    }

    public static void registerUser(HttpExchange exchange) throws IOException {
        byte[] requestBody = exchange.getRequestBody().readAllBytes();
        String[] strings = new String(requestBody).split(",");
        if (!validateUsername(exchange, strings)) return;
        getDbConnector().registerUser(strings[0], strings[1]);
        var user = getDbConnector().getUserByName(strings[0]);
        getDbConnector().createPlaylist(
                new Playlist("Saved tracks", "Tracks saved by the user " + user.getUsername(), user.getId(),
                        "img/playlist_cover/saved_tracks_cover.jpg"));
        sendStringResponse(exchange, 200, "Done");
    }

    private static boolean validateUsername(HttpExchange exchange, String[] strings) throws IOException {
        if (strings.length != 2 || Arrays.stream(strings).anyMatch(String::isEmpty)) {
            sendStringResponse(exchange, 400, "The body must be formatted in the following way: <username>,<psw>");
            return false;
        }
        if (!strings[0].matches("^[a-zA-Z0-9_]+")) {
            sendStringResponse(exchange, 400,
                    "The username specified is not valid (it should contain only numbers, letters and/or _");
            return false;
        }
        return true;
    }

    // endregion
    // region object CRUD
    public static void manageItem(HttpExchange exchange, Class<?> objType) throws IOException {
        var method = exchange.getRequestMethod().toLowerCase();
        switch (method) {
            case "get" -> {
                var queryParams = HttpHelper.queryParams(exchange);
                if (queryParams.containsKey("id"))
                    getObjectById(exchange, id -> getDbConnector().getObjectById(id, objType));
                else if (queryParams.containsKey("search")) searchObject(exchange, objType);
                else getObjects(exchange, numOfResults -> getDbConnector().getObjects(numOfResults, objType));
            }
            case "post" -> {
                if (objType.equals(Playlist.class)) createPlaylist(exchange);
                else sendStringResponse(exchange, 405,
                        "Cannot create a %s from a client".formatted(objType.getSimpleName().toLowerCase()));
            }
            case "put" -> putObject(exchange, objType);
            case "delete" -> {
                if (objType.equals(Playlist.class)) deletePlaylist(exchange);
                else sendStringResponse(exchange, 405,
                        "Cannot delete a %s from a client".formatted(objType.getSimpleName().toLowerCase()));
            }
            default -> sendStringResponse(exchange, 405, method + " is not supported for this path");
        }
    }

    public static void getObjectById(HttpExchange exchange, Function<Integer, ?> dbFunc) throws IOException {
        int id = Integer.parseInt(HttpHelper.queryParam(exchange, "id"));
        var object = dbFunc.apply(id);
        if (object == null) {
            sendStringResponse(exchange, 500, "Internal server error");
            return;
        }
        String json = getGson().toJson(object);
        sendStringResponse(exchange, 200, json);
    }

    public static void getObjects(HttpExchange exchange, Function<Integer, ?> dbFunc) throws IOException {
        int numberOfResults = Integer.parseInt(HttpHelper.queryParamOrDefault(exchange, "number-of-results", "20"));
        var objects = dbFunc.apply(numberOfResults);
        if (objects == null) {
            sendStringResponse(exchange, 500, "Internal server error");
            return;
        }
        String json = getGson().toJson(objects);
        sendStringResponse(exchange, 200, json);
    }

    public static void searchObject(HttpExchange exchange, Class<?> objType) throws IOException {
        var queryParams = queryParams(exchange);
        int numberOfResults = Integer.parseInt(queryParams.getOrDefault("number-of-results", "20"));
        var searchTerm = queryParams.getOrDefault("search", "");
        if (!queryParams.containsKey("field")) {
            sendStringResponse(exchange, 400,
                    "Missing 'field' query param, which must specify the name of the field to search for");
            return;
        }
        var columnName = queryParams.get("field");
        var objects = getDbConnector().searchObjects(numberOfResults, objType, columnName, searchTerm);
        String json = getGson().toJson(objects);
        sendStringResponse(exchange, 200, json);
    }

    public static <T> void putObject(HttpExchange exchange, Class<T> objType) throws IOException {
        var queryParams = queryParams(exchange);
        if (!queryParams.containsKey("id")) {
            sendStringResponse(exchange, 400, "The request must contain an id query parameter");
            return;
        }
        var id = Integer.parseInt(queryParams.get("id"));
        queryParams.remove("id");
        getDbConnector().updateItem(id, queryParams, objType);
        sendStringResponse(exchange, 200, "Item updated successfully");
    }

    public static <T> void manageItemSave(HttpExchange exchange, Class<T> objType) throws IOException {
        String method = exchange.getRequestMethod().toLowerCase();
        switch (method) {
            case "post" -> saveItem(exchange, objType);
            case "delete" -> unsaveItem(exchange, objType);
            default -> sendStringResponse(exchange, 405, method + " is not supported for this path");
        }
    }

    private static <T> void saveItem(HttpExchange exchange, Class<T> objType) throws IOException {
        try {
            var item = getItemToSave(exchange, objType);
            getDbConnector().createObject(item, objType, false);
            sendStringResponse(exchange, 200, "Item added");
        } catch (Exception e) {
            sendStringResponse(exchange, 400, "Exception while creating the object: " + e.getMessage());
        }
    }

    private static <T> void unsaveItem(HttpExchange exchange, Class<T> objType) throws IOException {
        try {
            var item = getItemToSave(exchange, objType);
            getDbConnector().deleteObject(item, objType);
            sendStringResponse(exchange, 200, "Item deleted");
        } catch (Exception e) {
            sendStringResponse(exchange, 400, "Exception while deleting the object: " + e.getMessage());
        }
    }

    private static <T> String[] getIdNames(HttpExchange exchange, Class<T> objType) {
        var queryParams = queryParams(exchange);
        var idNames = Arrays.stream(objType.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Column.class))
                .map(Field::getName).toArray(String[]::new);
        if (idNames.length != 2) {
            throw new IllegalArgumentException("Internal error: invalid class provided in the server");
        }
        for (var idName : idNames) {
            if (!queryParams.containsKey(idName)) {
                throw new IllegalArgumentException("Invalid request URL. It must contain: " + Arrays.toString(idNames));
            }
        }
        return idNames;
    }

    private static <T> T getItemToSave(HttpExchange exchange, Class<T> objType) throws Exception {
        var queryParams = queryParams(exchange);
        var idNames = getIdNames(exchange, objType);
        var ids = Arrays.stream(idNames).map(key -> Integer.parseInt(queryParams.get(key))).toArray(Integer[]::new);
        return objType.getDeclaredConstructor(Integer.class, Integer.class).newInstance(ids[0], ids[1]);
    }

    public static void getImg(HttpExchange exchange) throws IOException {
        var path = exchange.getRequestURI().getPath().replaceFirst("^.*img", "");
        var pathObj = Path.of("database/img" + path);
        if (!Files.exists(pathObj)) {
            sendStringResponse(exchange, 404, "File not found");
            return;
        }
        var imgBytes = Files.readAllBytes(pathObj);
        sendByteResponse(exchange, 200, imgBytes);
    }

    public static void getAudio(HttpExchange exchange) throws IOException {
        var path = exchange.getRequestURI().getPath().replaceFirst("^.*audio", "");
        byte[] responseBytes = Files.readAllBytes(Path.of("database/audio" + path));
        String contentType = path.endsWith("m3u8") ? "application/vnd.apple.mpegurl" : "audio/aac";
        exchange.getResponseHeaders().set("content-type", contentType);
        sendByteResponse(exchange, 200, responseBytes);
    }

    /**
     * Creates a playlist with these defaults:
     * <br/>name = 'Playlist #[number]' where [number] is progressive, to avoid name duplicates
     * <br/>description = 'Playlist from user [id]', where '[id]' is the id of the user creating the playlist
     * <br/>userId = [id], the id of the user
     * <br/>imgPath = [default], the default image for new playlists
     *
     * @param exchange The HTTP exchange object.
     *                 The request url must contain the field 'userId', the id of the user creating the playlist.
     *                 The id of the created playlist is then returned in the body of the response.
     * @throws IOException When there's an exception during the HTTP exchange
     */
    public static void createPlaylist(HttpExchange exchange) throws IOException {
        var ownerId = Integer.parseInt(queryParam(exchange, "userId"));
        var numberOfPlaylists = getDbConnector().getNumberOfPlaylistsWithName("Playlist #");
        String playlistName = "Playlist #%d".formatted(numberOfPlaylists);
        var playlist = new Playlist(playlistName, "Playlist from user %d".formatted(ownerId), ownerId,
                "img/playlist_cover/default_playlist.jpg");
        var id = getDbConnector().createPlaylist(playlist);
        sendStringResponse(exchange, 200, String.valueOf(id));
    }

    /**
     * Deletes a playlist, taking care of removing records from user_saved_playlist
     *
     * @param exchange The HTTP exchange object.
     *                 The request url must contain the field 'id', id of the playlist to delete.
     * @throws IOException When there's an exception during the HTTP exchange
     */
    public static void deletePlaylist(HttpExchange exchange) throws IOException {
        var playlistId = Integer.parseInt(queryParam(exchange, "id"));
        var playlist = getDbConnector().getObjectById(playlistId, Playlist.class);
        getDbConnector().deleteObject(new UserSavedPlaylist(playlist.getOwnerId(), playlistId),
                UserSavedPlaylist.class);
        getDbConnector().deleteObject(new Playlist(playlistId), Playlist.class);
        sendStringResponse(exchange, 200, "Playlist %d removed successfully".formatted(playlistId));
    }

    /**
     * Handles addition and deletion of tracks in playlists
     *
     * @param exchange The HTTP exchange object.
     *                 The request url must contain the fields 'id', id of the playlist and 'trackId', id of the track
     *                 to add.
     * @throws IOException When there's an exception during the HTTP exchange
     */
    public static void managePlaylist(HttpExchange exchange) throws IOException {
        var method = exchange.getRequestMethod().toLowerCase();
        var queryParams = queryParams(exchange);
        var date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        var playlistId = Integer.parseInt(queryParams.get("id"));
        var trackId = Integer.parseInt(queryParams.get("trackId"));
        var relationship = new TrackInPlaylist(trackId, playlistId, date);
        switch (method) {
            case "post" -> {
                getDbConnector().createObject(relationship, TrackInPlaylist.class, false);
                sendStringResponse(exchange, 200, "Track %d added to playlist %d".formatted(trackId, playlistId));
            }
            case "delete" -> {
                getDbConnector().deleteObject(relationship, TrackInPlaylist.class);
                sendStringResponse(exchange, 200, "Track %d deleted from playlist %d".formatted(trackId, playlistId));
            }
            default -> sendStringResponse(exchange, 405, method + " is not supported for this path");
        }
    }

    /**
     * Invoked when no other routes are matched.
     *
     * @param exchange The HTTP exchange
     * @throws IOException When there's an exception during the HTTP exchange
     */
    public static void defaultRoute(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestURI());
        sendStringResponse(exchange, 200, "Welcome to Lithium!");
    }

    //endregion
}
