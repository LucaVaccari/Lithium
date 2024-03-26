package it.unibs.pajc.lithium.connection;

import it.unibs.pajc.lithium.db.om.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionReceiver implements Runnable {
    private final int port;
    private boolean running = true;

    private static final Set<Connection> connections = ConcurrentHashMap.newKeySet();

    public ConnectionReceiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Listening for upcoming connections on port: " + port);

        Connection connection = null;
        try (var welcomeSocket = new ServerSocket(port)) {
            while (running) {
                var socket = welcomeSocket.accept();
                connection = new Connection(socket);
                connections.add(connection);
            }
        } catch (IOException e) {
            connections.remove(connection);
            e.printStackTrace();
        }
    }

    public static void remove(Connection connection) {
        connections.remove(connection);
    }

    public static boolean duplicateUser(User user) {
        return connections.stream().anyMatch(c -> c.getUser() != null && c.getUser().equals(user));
    }

    public void interrupt() {
        for (Connection conn : connections) {
            conn.interrupt();
        }
        connections.clear();
        running = false;
    }
}
