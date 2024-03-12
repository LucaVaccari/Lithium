package it.unibs.pajc.lithium;

import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.function.Function;

import static it.unibs.pajc.lithium.ServerMain.getDbConnector;
import static it.unibs.pajc.lithium.ServerMain.getGson;

public class HttpRoutes {
    //region context methods
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
        try {
            getDbConnector().registerUser(strings[0], strings[1]);
            res.status(200);
        } catch (IllegalArgumentException e) {
            res.status(400);
            res.body(e.getMessage());
            return res.body();
        }
        return "Done";
    }

    public static <T> String getObjects(Request req, Response res, Function<Integer, T[]> dbFunc) {
        try {
            int numberOfResults = Integer.parseInt(req.queryParamOrDefault("number-of-results", "20"));
            var objects = dbFunc.apply(numberOfResults);
            String json = getGson().toJson(objects);
            res.status(200);
            res.body(json);
            return json;
        } catch (NumberFormatException e) {
            res.status(400);
            res.body("Some of the provided fields cannot be converted to integers");
            return "ERROR";
        }
    }

    public static String getPlaylistsInfo(Request req, Response res) {
        return getObjects(req, res, getDbConnector()::getAllPlaylists);
    }

    public static String getAlbumsInfo(Request req, Response res) {
        return getObjects(req, res, getDbConnector()::getAllAlbums);
    }

    public static String getArtistsInfo(Request req, Response res) {
        return getObjects(req, res, getDbConnector()::getAllArtists);
    }

    //endregion
}
