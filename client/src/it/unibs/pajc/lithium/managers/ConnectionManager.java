package it.unibs.pajc.lithium.managers;

import it.unibs.pajc.lithium.Config;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.util.NoReturnFunction;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the TCP connection with the server
 */
public class ConnectionManager implements Runnable {
    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private boolean running = true;

    private static final Map<String, NoReturnFunction<String>> commands = new HashMap<>();

    static {
        // todo partySync
        // todo partyChat
        // todo partyTrack
        // todo partyUsers
        // todo allParties
        // todo hostUpdated
        commands.put("error", m -> Platform.runLater(() -> AlertUtil.showErrorAlert("Error", "Session error", m)));
    }

    public ConnectionManager() {
        try {
            socket = new Socket(Config.getServerUrl(), Config.getServerPort());
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                var inputTokens = input.strip().split(";;");
                if (inputTokens.length > 2 || inputTokens.length == 0) {
                    writeMessage("error;;The message must be formed in the following way: <command>;;<body>");
                    continue;
                }
                commands.get(inputTokens[0]).apply(inputTokens[1]);
            } catch (IOException e) {
                interrupt();
            }
        }
    }

    public void writeMessage(String message) {
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
