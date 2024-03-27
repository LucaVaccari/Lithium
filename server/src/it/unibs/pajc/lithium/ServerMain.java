package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import it.unibs.pajc.lithium.connection.LcpServer;
import it.unibs.pajc.lithium.db.DbConnector;
import it.unibs.pajc.lithium.http.HttpServerManager;

import java.io.IOException;

public class ServerMain {
    public static final int HTTP_PORT = 8080;
    private static final DbConnector dbConnector = new DbConnector();
    private static final Gson gson = new Gson();
    public static final int SOCKET_PORT = 1234;

    public static void main(String[] args) {
        try {
            dbConnector.connect("jdbc:sqlite:database\\lithium.sqlite");
            HttpServerManager.start(HTTP_PORT);
            new Thread(new LcpServer(SOCKET_PORT)).start();
        } catch (IOException e) {
            Logger.logError(null, e);
        }
    }

    public static DbConnector getDbConnector() {
        return dbConnector;
    }

    public static Gson getGson() {
        return gson;
    }
}
