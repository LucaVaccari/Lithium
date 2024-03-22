package it.unibs.pajc.lithium;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpHelper {
    public static Map<String, String> queryParams(HttpExchange exchange) {
        if (exchange == null) {
            return null;
        }
        var query = exchange.getRequestURI().getQuery();
        var result = new HashMap<String, String>();
        if (query == null) return result;
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) result.put(entry[0], entry[1]);
        }
        return result;
    }

    public static String queryParam(HttpExchange exchange, String param) {
        return queryParams(exchange).get(param);
    }

    public static String queryParamOrDefault(HttpExchange exchange, String param, String defaultVal) {
        var map = queryParams(exchange);
        return map.getOrDefault(param, defaultVal);
    }

    public static void sendStringResponse(HttpExchange exchange, int code, String body) throws IOException {
        var response = body.getBytes();
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
    }

    public static void sendByteResponse(HttpExchange exchange, int code, byte[] body) throws IOException {
        exchange.sendResponseHeaders(code, body.length);
        exchange.getResponseBody().write(body);
    }
}
