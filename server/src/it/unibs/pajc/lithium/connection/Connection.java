package it.unibs.pajc.lithium.connection;

import it.unibs.pajc.util.NoReturnFunction2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Connection {
    private final Socket socket;
    private final PrintWriter writer;
    private static final Map<String, NoReturnFunction2<String, Connection>> commands = new ConcurrentHashMap<>();

    static {
        commands.put("hello", (s, c) -> c.writeMessage("world"));
    }

    private boolean running = true;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                System.out.println("New connection with " + socket.getInetAddress().getHostAddress());
                while (running) {
                    try {
                        var input = reader.readLine();
                        System.out.println(input);
                        var inputTokens = input.strip().split(";;");
                        if (inputTokens.length > 2 || inputTokens.length == 0) {
                            // TODO: send error
                        } else {
                            commands.get(inputTokens[0]).apply(inputTokens[1], this);
                        }
                    } catch (IOException e) {
                        interrupt();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeMessage(String message) {
        writer.println(message);
    }

    private Socket getSocket() {
        return socket;
    }

    public void interrupt() {
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
