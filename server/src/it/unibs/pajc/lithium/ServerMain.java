package it.unibs.pajc.lithium;

import com.sun.net.httpserver.HttpServer;
import it.unibs.pajc.lithium.db.DbConnector;
import it.unibs.pajc.lithium.db.om.Album;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerMain {
    public static void main(String[] args) {
        // DB TEST
        try (var dbConnector = new DbConnector()) {
            dbConnector.connect("jdbc:sqlite:..\\database\\lithium.sqlite");
            System.out.println((dbConnector.getAlbumById(4)));
        } catch (Exception ignored) {
        }
        // END DB TEST
        // HTTP TEST
        try {
            var server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/test", httpExchange -> {
                System.out.println(httpExchange.getRequestMethod());
                System.out.println("Song requested");
                httpExchange.getResponseHeaders().set("content-type", "application/vnd.apple.mpegurl");
                byte[] response = M3U8Test.getPlaylist();
                boolean get = httpExchange.getRequestMethod().equals("GET");
                httpExchange.sendResponseHeaders(200, get ? response.length : -1);
                if (get) httpExchange.getResponseBody().write(response);
                httpExchange.getResponseBody().close();
            });

            server.createContext("/audio", httpExchange -> {
                String uri = httpExchange.getRequestURI().toString();
                System.out.println(uri);
                String fileName = uri.replace("/audio", "");
                httpExchange.getResponseHeaders().set("content-type", "audio/aac");
                byte[] response = Files.readAllBytes(Path.of("C:/Users/asus/Desktop/SuckYourLollipop/" + fileName));
                boolean get = httpExchange.getRequestMethod().equals("GET");
                httpExchange.sendResponseHeaders(200, get ? response.length : -1);
                if (get) httpExchange.getResponseBody().write(response);
                httpExchange.getResponseBody().close();
            });

            server.start();
            System.out.println("Server started");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // END TEST
    }
}
