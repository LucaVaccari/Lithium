package it.unibs.pajc;

import kong.unirest.*;

import java.util.Base64;
import java.util.HashMap;

/**
 * Program-independent class providing methods for sending HTTP messages
 */
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
    public static String get(String subURL) throws UnirestException {
        GetRequest getRequest = Unirest.get(buildUrl(subURL));
        var response = getRequest.asString();
        return response.isSuccess() ? response.getBody() : null;
    }

    /**
     * Sends a GET message to the server
     *
     * @param subURL  the URI to append after the server address
     * @param queries query params to add to the subURL (?param=value)
     * @return The response body received
     */
    public static String get(String subURL, HashMap<String, String> queries) {
        GetRequest getRequest = Unirest.get(buildUrl(subURL));
        addFilters(getRequest, queries);
        var response = getRequest.asString();
        return response.isSuccess() ? response.getBody() : null;
    }

    public static String post(String subURL, String body) {
        HttpRequestWithBody postRequest = Unirest.post(buildUrl(subURL));
        return postRequest.body(body).asString().getBody();
    }

    public static String put(String subURL, String body, int id) {
        HttpRequestWithBody putRequest = Unirest.put(buildUrl(subURL) + "/" + id);
        return putRequest.body(body).asString().getBody();
    }

    public static String delete(String subURL, int id) {
        HttpRequestWithBody deleteRequest = Unirest.delete(buildUrl(subURL) + "/" + id);
        return deleteRequest.asString().getBody();
    }

    /***
     * Builds a URL to the server by appending the subURL to the base URL.
     * DON'T PUT A '/' AT THE START OF THE SUBURL, it is added automatically.
     * @param subUrl The subURL to append at the end of the base URL
     * @return The URL built
     */
    public static String buildUrl(String subUrl) {
        return URL + "/" + subUrl;
    }

    public static byte[] getBase64Img(String subUrl) {
        var encodedImgString = get(subUrl);
        return encodedImgString != null ? Base64.getDecoder().decode(encodedImgString) : new byte[0];
    }
}
