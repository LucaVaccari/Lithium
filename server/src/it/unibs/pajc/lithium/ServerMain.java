package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import it.unibs.pajc.lithium.db.DbConnector;
import it.unibs.pajc.lithium.db.om.*;

import static spark.Spark.*;

public class ServerMain {
    private static final DbConnector dbConnector = new DbConnector();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        dbConnector.connect("jdbc:sqlite:database\\lithium.sqlite");
        port(8080);
        path("/user", () -> {
            get("/exists", HttpRoutes::userExists);
            get("/:id", (req, res) -> HttpRoutes.getObjectById(req, res,
                    id -> getDbConnector().getObjectById(id, User.class, "user_id")));
            post("/auth", HttpRoutes::authenticateUser);
            post("/register", HttpRoutes::registerUser);
        });

        path("track", () -> {
            get("/search", (req, res) -> HttpRoutes.searchObject(req, res, Track.class, "track_title"));
            get("/:id", (req, res) -> HttpRoutes.getObjectById(req, res,
                    id -> getDbConnector().getObjectById(id, Track.class, "track_id")));
        });
        path("album", () -> {
            get("/search", (req, res) -> HttpRoutes.searchObject(req, res, Album.class, "album_title"));
            get("/:id", (req, res) -> HttpRoutes.getObjectById(req, res,
                    id -> getDbConnector().getObjectById(id, Album.class, "album_id")));
        });
        path("artist", () -> {
            get("/search", (req, res) -> HttpRoutes.searchObject(req, res, Artist.class, "artist_name"));
            get("/:id", (req, res) -> HttpRoutes.getObjectById(req, res,
                    id -> getDbConnector().getObjectById(id, Artist.class, "artist_id")));
        });
        path("playlist", () -> {
            get("/search", (req, res) -> HttpRoutes.searchObject(req, res, Playlist.class, "playlist_title"));
            get("/:id", (req, res) -> HttpRoutes.getObjectById(req, res,
                    id -> getDbConnector().getObjectById(id, Playlist.class, "playlist_id")));
        });

        get("/track", (req, res) -> HttpRoutes.getObjects(req, res,
                numOfResults -> getDbConnector().getObjects(numOfResults, Track.class)));
        get("/album", (req, res) -> HttpRoutes.getObjects(req, res,
                numOfResults -> getDbConnector().getObjects(numOfResults, Album.class)));
        get("/artist", (req, res) -> HttpRoutes.getObjects(req, res,
                numOfResults -> getDbConnector().getObjects(numOfResults, Artist.class)));
        get("/playlist", (req, res) -> HttpRoutes.getObjects(req, res,
                numOfResults -> getDbConnector().getObjects(numOfResults, Playlist.class)));

        get("/img/*/*", HttpRoutes::getImg);

        exception(Exception.class, (e, req, res) -> {
            e.printStackTrace();
            res.status(500);
            res.body(e.getMessage());
        });

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
