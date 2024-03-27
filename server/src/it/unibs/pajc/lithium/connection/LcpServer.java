package it.unibs.pajc.lithium.connection;

import it.unibs.pajc.lithium.Logger;
import it.unibs.pajc.lithium.db.om.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LcpServer implements Runnable {
    private final int port;
    private boolean running = true;

    private static final Set<LcpConnection> connections = ConcurrentHashMap.newKeySet();

    public LcpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        Logger.log("Listening for upcoming connections on port: " + port);

        LcpConnection connection = null;
        try (var welcomeSocket = new ServerSocket(port)) {
            while (running) {
                var socket = welcomeSocket.accept();
                connection = new LcpConnection(socket);
                connections.add(connection);
            }
        } catch (IOException e) {
            connections.remove(connection);
            Logger.logError(this, e);
        }
    }

    public static Set<LcpConnection> getConnections() {
        return Collections.unmodifiableSet(connections);
    }

    public static void remove(LcpConnection connection) {
        connections.remove(connection);
    }

    public static boolean duplicateUser(User user) {
        return connections.stream().anyMatch(c -> c.getUser() != null && c.getUser().equals(user));
    }

    public void interrupt() {
        for (LcpConnection conn : connections) {
            conn.interrupt();
        }
        connections.clear();
        running = false;
    }
}
