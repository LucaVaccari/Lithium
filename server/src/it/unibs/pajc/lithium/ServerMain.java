package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import it.unibs.pajc.lithium.db.DbConnector;

import static spark.Spark.*;

public class ServerMain {
    private static DbConnector dbConnector;
    private static Gson gson;

    public static void main(String[] args) {
        dbConnector = new DbConnector();
        dbConnector.connect("jdbc:sqlite:..\\database\\lithium.sqlite");
        port(8080);
        path("/user", () -> {
            get("/exists", HttpRoutes::userExists);
            post("/auth", HttpRoutes::authenticateUser);
            post("/register", HttpRoutes::registerUser);
        });

        get("/playlists", HttpRoutes::getPlaylists);

        // TODO: show available calls
        get("/", (req, res) -> "Welcome to Lithium!");


        // DB TEST
//        try (var dbConnector = new DbConnector()) {
//            dbConnector.connect("jdbc:sqlite:..\\database\\lithium.sqlite");
//            System.out.println((dbConnector.getAlbumById(4)));
//        } catch (Exception ignored) {
//        }
        // END DB TEST
        // HTTP TEST
//        try {
//            var server = HttpServer.create(new InetSocketAddress(8080), 0);
//            server.createContext("/test", httpExchange -> {
//                System.out.println(httpExchange.getRequestMethod());
//                System.out.println("Song requested");
//                httpExchange.getResponseHeaders().set("content-type", "application/vnd.apple.mpegurl");
//                byte[] response = M3U8Test.getPlaylist();
//                boolean get = httpExchange.getRequestMethod().equals("GET");
//                httpExchange.sendResponseHeaders(200, get ? response.length : -1);
//                if (get) httpExchange.getResponseBody().write(response);
//                httpExchange.getResponseBody().close();
//            });
//
//            server.createContext("/audio", httpExchange -> {
//                String uri = httpExchange.getRequestURI().toString();
//                System.out.println(uri);
//                String fileName = uri.replace("/audio", "");
//                httpExchange.getResponseHeaders().set("content-type", "audio/aac");
//                byte[] response = Files.readAllBytes(Path.of("C:/Users/asus/Desktop/SuckYourLollipop/" + fileName));
//                boolean get = httpExchange.getRequestMethod().equals("GET");
//                httpExchange.sendResponseHeaders(200, get ? response.length : -1);
//                if (get) httpExchange.getResponseBody().write(response);
//                httpExchange.getResponseBody().close();
//            });
//
//            server.start();
//            System.out.println("Server started");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        // END TEST
    }

    public static DbConnector getDbConnector() {
        return dbConnector;
    }

    public static Gson getGson() {
        return gson;
    }
}
