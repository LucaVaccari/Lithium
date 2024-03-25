package it.unibs.pajc.lithium;

import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

/**
 * Utility class providing methods for sending HTTP messages
 */
public class HttpHandler {
    // todo Unirest.config().defaultBaseUrl("http://...");
    private final static String URL;

    static {
        URL = Config.getServerHttpURL();
    }

    /**
     * Sends a GET request to the server
     *
     * @param subURL the URI to append after the root server address.
     *               If '/' is not present at the start of the subUrl, it is added automatically.
     * @return The response body received
     */
    public static String get(String subURL) throws UnirestException {
        GetRequest getRequest = Unirest.get(buildUrl(subURL));
        var response = getRequest.asString();
        return response.isSuccess() ? response.getBody() : null;
    }

    /**
     * Sends a POST request to the server
     *
     * @param subURL the URI to append after the root server address.
     *               If '/' is not present at the start of the subUrl, it is added automatically.
     * @param body   The body of the request
     * @return The response body received
     */
    public static String post(String subURL, String body) {
        HttpRequestWithBody postRequest = Unirest.post(buildUrl(subURL));
        return postRequest.body(body).asString().getBody();
    }

    /**
     * Sends a POST request to the server
     *
     * @param subURL the URI to append after the root server address.
     *               If '/' is not present at the start of the subUrl, it is added automatically.
     * @return The response body received
     */
    public static String post(String subURL) {
        return post(subURL, "");
    }

    public static String put(String subURL) {
        HttpRequestWithBody putRequest = Unirest.put(buildUrl(subURL));
        return putRequest.asString().getBody();
    }

    /**
     * Sends a DELETE request to the server
     *
     * @param subURL the URI to append after the root server address.
     *               If '/' is not present at the start of the subUrl, it is added automatically.
     * @return The response body received
     */
    public static String delete(String subURL) {
        HttpRequestWithBody deleteRequest = Unirest.delete(buildUrl(subURL));
        return deleteRequest.asString().getBody();
    }

    /***
     * Builds a URL to the server by appending the subURL to the base URL.
     * If '/' is not present at the start of the subUrl, it is added automatically.
     * @param subUrl The subURL to append at the end of the base URL
     * @return The URL built
     */
    public static String buildUrl(String subUrl) {
        return URL + (subUrl.startsWith("/") ? "" : "/") + subUrl;
    }
}
