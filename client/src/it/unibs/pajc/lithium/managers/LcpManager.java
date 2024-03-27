package it.unibs.pajc.lithium.managers;

import it.unibs.pajc.lithium.Config;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Track;
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
 * Handles the LCP connection with the server
 */
public class LcpManager implements Runnable {
    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private boolean running = true;

    private static final Map<String, NoReturnFunction<String>> commands = new HashMap<>();

    static {
        commands.put("partyId", body -> PartyManager.setId(Integer.parseInt(body)));
        commands.put("partySync", body -> PartyManager.syncParty(Double.parseDouble(body)));
        commands.put("partyChat", PartyManager::partyChatReceived);
        commands.put("partyTrack",
                body -> PartyManager.setCurrentTrack(ItemProvider.getItem(Integer.parseInt(body), Track.class)));
        commands.put("pause", body -> {
            switch (body) {
                case "pause" -> PartyManager.pause(true);
                case "unpause" -> PartyManager.pause(false);
                default -> System.err.println("Unexpected lcp pause msg body: " + body);
            }
        });
        commands.put("userUpdate", PartyManager::userUpdate);
        commands.put("allParties", PartyManager::partiesUpdate);
        // todo hostUpdated
        commands.put("hostUpdated", body -> PartyManager.updateHost(Integer.parseInt(body)));
        commands.put("error", m -> Platform.runLater(() -> AlertUtil.showErrorAlert("Error", "LCP error", m)));

        // todo block playback when in party
    }

    public LcpManager() {
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
                if (inputTokens.length != 2) {
                    System.err.println(
                            "The message must be formed in the following way: <command>;;<body>. Message: " + input);
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
