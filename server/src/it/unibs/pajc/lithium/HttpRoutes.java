package it.unibs.pajc.lithium;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Function;

import static it.unibs.pajc.lithium.HttpHelper.*;
import static it.unibs.pajc.lithium.ServerMain.getDbConnector;
import static it.unibs.pajc.lithium.ServerMain.getGson;

public class HttpRoutes {
    // TODO: check calling methods
    //region user authentication
    public static void userExists(HttpExchange exchange) throws IOException {
        String username = HttpHelper.queryParam(exchange, "username");
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
        var queryParams = HttpHelper.queryParams(exchange);
        if (queryParams.containsKey("id")) getObjectById(exchange, id -> getDbConnector().getObjectById(id, objType));
        else if (queryParams.containsKey("search")) searchObject(exchange, objType);
        else getObjects(exchange, numOfResults -> getDbConnector().getObjects(numOfResults, objType));
    }

    public static void getObjectById(HttpExchange exchange, Function<Integer, ?> dbFunc) throws IOException {
        int id = Integer.parseInt(HttpHelper.queryParam(exchange, "id"));
        var objects = dbFunc.apply(id);
        if (objects == null) {
            sendStringResponse(exchange, 500, "Internal server error");
            return;
        }
        String json = getGson().toJson(objects);
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
        System.out.println(exchange.getRequestURI());
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

    public static void defaultRoute(HttpExchange exchange) throws IOException {
        sendStringResponse(exchange, 200, "Welcome to Lithium!");
    }

    //endregion
}
