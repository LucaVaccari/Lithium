package it.unibs.pajc.lithium.http;

import com.sun.net.httpserver.HttpServer;
import it.unibs.pajc.lithium.Logger;
import it.unibs.pajc.lithium.ServerMain;
import it.unibs.pajc.lithium.db.om.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class HttpServerManager {
    public static void start(int port) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(ServerMain.HTTP_PORT), 0);
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
        // OTHER
        server.createContext("/playlist/add", HttpRoutes::managePlaylist);
        // TODO: show available calls
        server.createContext("/", HttpRoutes::defaultRoute);

        server.start();
        Logger.log("Http server listening on port: " + port);
    }

    private HttpServerManager() {
    }
}
