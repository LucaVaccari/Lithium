package it.unibs.pajc.lithium;

import spark.Request;
import spark.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Function;

import static it.unibs.pajc.lithium.ServerMain.getDbConnector;
import static it.unibs.pajc.lithium.ServerMain.getGson;

public class HttpRoutes {
    //region user authentication
    public static boolean userExists(Request req, Response res) {
        String username = req.queryParams("username");
        if (!username.matches("^[a-zA-Z0-9_]+")) {
            res.status(400);
            res.body("The username specified is not valid (it should contain only numbers, letters and/or _");
            return false;
        }
        return getDbConnector().getUserByName(username) != null;
    }

    public static boolean authenticateUser(Request req, Response res) {
        String[] strings = req.body().split(",");
        if (strings.length != 2 || Arrays.stream(strings).anyMatch(String::isEmpty)) {
            res.status(400);
            res.body("The body must be formatted in the following way: <username>,<psw>");
            return false;
        }
        if (!strings[0].matches("^[a-zA-Z0-9_]+")) {
            res.status(400);
            res.body("The username specified is not valid (it should contain only numbers, letters and/or _");
            return false;
        }
        return getDbConnector().authenticateUser(strings[0], strings[1]);
    }

    public static Object registerUser(Request req, Response res) {
        String[] strings = req.body().split(",");
        if (strings.length != 2 || Arrays.stream(strings).anyMatch(String::isEmpty)) {
            res.status(400);
            res.body("The body must be formatted in the following way: <username>,<psw>");
            return res.body();
        }
        if (!strings[0].matches("^[a-zA-Z0-9_]+")) {
            res.status(400);
            res.body("The username specified is not valid (it should contain only numbers, letters and/or _");
            return res.body();
        }
        getDbConnector().registerUser(strings[0], strings[1]);
        res.status(200);
        return "Done";
    }

    // endregion
    // region object CRUD

    public static String getObjects(Request req, Response res, Function<Integer, ?> dbFunc) {
        int numberOfResults = Integer.parseInt(req.queryParamOrDefault("number-of-results", "20"));
        var objects = dbFunc.apply(numberOfResults);
        String json = getGson().toJson(objects);
        res.status(200);
        res.body(json);
        return json;
    }

    public static String getObjectById(Request req, Response res, Function<Integer, ?> dbFunc) {
        int id = Integer.parseInt(req.params("id"));
        var objects = dbFunc.apply(id);
        String json = getGson().toJson(objects);
        res.status(200);
        res.body(json);
        return json;
    }

    public static String searchObject(Request req, Response res, Class<?> objType, String columnName) {
        int numberOfResults = Integer.parseInt(req.queryParamOrDefault("number-of-results", "20"));
        var searchTerm = req.queryParamOrDefault("search", "");
        var objects = getDbConnector().searchObjects(numberOfResults, objType, columnName, searchTerm);
        String json = getGson().toJson(objects);
        res.status(200);
        res.body(json);
        return json;
    }

    public static String getImg(Request req, Response res) throws IOException {
        var path = String.join("/", req.splat());
        Path pathObj = Path.of("database/img/" + path);
        if (!Files.exists(pathObj)) {
            res.status(404);
            return null;
        }
        var imgBytes = Files.readAllBytes(pathObj);
        var imgString = Base64.getEncoder().encodeToString(imgBytes);
        res.status(200);
        res.body(imgString);
        return imgString;
    }

    //endregion
}
