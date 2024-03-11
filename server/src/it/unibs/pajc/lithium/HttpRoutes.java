package it.unibs.pajc.lithium;

import spark.Request;
import spark.Response;

import java.util.Arrays;

import static it.unibs.pajc.lithium.ServerMain.getDbConnector;
import static it.unibs.pajc.lithium.ServerMain.getGson;

public class HttpRoutes {
    //region context methods
    public static boolean userExists(Request req, Response ignoredRes) {
        String username = req.queryParams("username");
        return getDbConnector().getUserByName(username) != null;
    }

    public static boolean authenticateUser(Request req, Response res) {
        String[] strings = req.body().split(",");
        if (strings.length != 2 || Arrays.stream(strings).anyMatch(String::isEmpty)) {
            res.status(400);
            res.body("The body must be formatted in the following way: <username>,<psw>");
            return false;
        }
        return getDbConnector().authenticateUser(strings[0], strings[1]);
    }

    public static Object registerUser(Request req, Response res) {
        String[] strings = req.body().split(",");
        if (strings.length != 2 || Arrays.stream(strings).anyMatch(String::isEmpty)) {
            res.status(400);
            res.body("The body must be formatted in the following way: <username>,<psw>");
            return false;
        }
        try {
            getDbConnector().registerUser(strings[0], strings[1]);
        } catch (IllegalArgumentException e) {
            res.status(400);
            res.body(e.getMessage());
            return false;
        }
        return "Done";
    }

    public static String getPlaylists(Request req, Response res) {
        try {
            int numberOfResults = Integer.parseInt(req.queryParamOrDefault("number-of-results", "20"));
            var playlists = getDbConnector().getAllPlaylists(numberOfResults);
            String json = getGson().toJson(playlists);
            System.out.println(json);
            res.status(200);
            res.body(json);
            return json;
        } catch (NumberFormatException e) {
            res.status(400);
            res.body("Some of the provided fields cannot be converted to integers");
            return "ERROR";
        }
    }
    //endregion
}
