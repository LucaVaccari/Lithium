package it.unibs.pajc;

import it.unibs.pajc.lithium.Config;
import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

/**
 * Program-independent class providing methods for sending HTTP messages
 */
public class HttpHandler {
    // todo Unirest.config().defaultBaseUrl("http://...");
    private final static String URL;

    static {
        URL = Config.getServerUrl();
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
}
