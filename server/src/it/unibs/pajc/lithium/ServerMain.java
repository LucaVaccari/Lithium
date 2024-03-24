package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import it.unibs.pajc.lithium.db.DbConnector;
import it.unibs.pajc.lithium.db.om.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerMain {
    private static final DbConnector dbConnector = new DbConnector();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            dbConnector.connect("jdbc:sqlite:database\\lithium.sqlite");
            var server = HttpServer.create(new InetSocketAddress(8080), 0);
            // USER CTX
            server.createContext("/user/exists", HttpRoutes::userExists);
            server.createContext("/user/auth", HttpRoutes::authenticateUser);
            server.createContext("/user/register", HttpRoutes::registerUser);
            server.createContext("/user/save/album", e -> HttpRoutes.manageItemSave(e, UserSavedAlbum.class));
            server.createContext("/user/save/playlist", e -> HttpRoutes.manageItemSave(e, UserSavedPlaylist.class));
            server.createContext("/user/save/artist", e -> HttpRoutes.manageItemSave(e, ArtistFollower.class));
            // ITEM CTX
            server.createContext("/user", e -> HttpRoutes.manageItem(e, User.class));
            server.createContext("/track", e -> HttpRoutes.manageItem(e, Track.class));
            server.createContext("/album", e -> HttpRoutes.manageItem(e, Album.class));
            server.createContext("/artist", e -> HttpRoutes.manageItem(e, Artist.class));
            server.createContext("/playlist", e -> HttpRoutes.manageItem(e, Playlist.class));
            server.createContext("/genre", e -> HttpRoutes.manageItem(e, Genre.class));
            // DATA CTX
            server.createContext("/img", HttpRoutes::getImg);
            server.createContext("/audio", HttpRoutes::getAudio);

            // TODO: show available calls
            server.createContext("/", HttpRoutes::defaultRoute);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DbConnector getDbConnector() {
        return dbConnector;
    }

    public static Gson getGson() {
        return gson;
    }
}
