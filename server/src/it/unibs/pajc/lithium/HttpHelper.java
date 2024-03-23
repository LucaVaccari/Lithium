package it.unibs.pajc.lithium;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        var response = body.getBytes(StandardCharsets.UTF_8);
        sendByteResponse(exchange, code, response);
    }

    public static void sendByteResponse(HttpExchange exchange, int code, byte[] body) throws IOException {
        var sendBody = !exchange.getRequestMethod().equalsIgnoreCase("head");
        exchange.sendResponseHeaders(code, sendBody ? body.length : -1);
        if (sendBody) exchange.getResponseBody().write(body);
        exchange.close();
    }
}
