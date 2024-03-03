package it.unibs.pajc.lithium;

import kong.unirest.GetRequest;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;

import java.util.HashMap;

public class HttpHandler {
    // todo Unirest.config().defaultBaseUrl("http://...");
    // todo set url and port in config or settings
    private static final String URL = "http://localhost:8080";

    private static void addFilters(HttpRequest<?> request, HashMap<String, String> queries) {
        for (var entry : queries.entrySet()) {
            request = request.queryString(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sends a GET message to the server
     *
     * @param subURL the URI to append after the server address
     * @return The response body received
     */
    public static String get(String subURL) {
        GetRequest getRequest = Unirest.get(URL + subURL);
        return getRequest.asString().getBody();
    }

    /**
     * Sends a GET message to the server
     *
     * @param subURL  the URI to append after the server address
     * @param queries query params to add to the subURL (?param=value)
     * @return The response body received
     */
    public static String get(String subURL, HashMap<String, String> queries) {
        GetRequest getRequest = Unirest.get(URL + subURL);
        addFilters(getRequest, queries);
        return getRequest.asString().getBody();
    }

    public static String post(String subURL, String body) {
        HttpRequestWithBody postRequest = Unirest.post(URL + subURL);
        return postRequest.body(body).asString().getBody();
    }

    public static String put(String subURL, String body, int id) {
        HttpRequestWithBody putRequest = Unirest.put(URL + subURL + "/" + id);
        return putRequest.body(body).asString().getBody();
    }

    public static String delete(String subURL, int id) {
        HttpRequestWithBody deleteRequest = Unirest.delete(URL + subURL + "/" + id);
        return deleteRequest.asString().getBody();
    }
}
