package it.unibs.pajc.lithium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionManager implements Runnable {
    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private boolean running = true;

    public ConnectionManager() {
        try {
            socket = new Socket("localhost", Config.getServerPort());
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            write("hello;;test");
            System.out.println("Connected to " + socket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                var input = reader.readLine();
                System.out.println(input);
            } catch (IOException e) {
                interrupt();
            }
        }
    }

    public void write(String message) {
        writer.println(message.strip());
    }

    private void interrupt() {
        try {
            running = false;
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
