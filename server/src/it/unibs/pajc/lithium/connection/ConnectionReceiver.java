package it.unibs.pajc.lithium.connection;

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

        try (var welcomeSocket = new ServerSocket(port)) {
            while (running) {
                var socket = welcomeSocket.accept();
                connections.add(new Connection(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void interrupt() {
        for (Connection conn : connections) {
            conn.interrupt();
        }
        running = false;
    }
}
