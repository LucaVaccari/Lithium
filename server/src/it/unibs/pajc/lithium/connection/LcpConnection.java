package it.unibs.pajc.lithium.connection;

import it.unibs.pajc.lithium.ServerMain;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.util.NoReturnFunction2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LcpConnection {
    private final Socket socket;
    private final PrintWriter writer;
    private static final Map<String, NoReturnFunction2<String, LcpConnection>> commands = new ConcurrentHashMap<>();
    private boolean running = true;

    private User user;

    static {
        commands.put("hello", (body, connection) -> connection.writeMessage("world"));
        commands.put("auth", (body, connection) -> {
            if (connection.user != null) {
                connection.writeMessage("error;;The user is already authenticated");
                return;
            }
            var tokens = body.split("::");
            if (tokens.length != 2) {
                connection.writeMessage("error;;The body of the message must be formed in the following way: " +
                        "<username>::<passwordHash>");
            }
            int userid = ServerMain.getDbConnector().authenticateUser(tokens[0], tokens[1]);
            if (userid == -1) {
                connection.writeMessage("error;;User not authenticated: wrong credentials");
                return;
            }
            User newUser = ServerMain.getDbConnector().getObjectById(userid, User.class);
            if (LcpServer.duplicateUser(newUser)) {
                connection.writeMessage("error;;User is already authenticated on another socket");
                return;
            }
            connection.user = newUser;
            System.out.println(connection.user.getUsername() + " authenticated");
        });
        commands.put("joinParty", ServerPartyManager::joinParty);
        commands.put("leaveParty", ServerPartyManager::leaveParty);
        commands.put("syncParty", ServerPartyManager::syncParty);
        commands.put("partyTrack", ServerPartyManager::updateTrack);
        commands.put("pause", ServerPartyManager::pause);
        commands.put("partyChat", ServerPartyManager::chat);
        commands.put("allParties", ServerPartyManager::allParties);
    }

    public LcpConnection(Socket socket) {
        this.socket = socket;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                System.out.println("New connection with " + socket.getInetAddress().getHostAddress());
                while (running) {
                    try {
                        var input = reader.readLine();
                        var inputTokens = input.strip().split(";;");
                        if (inputTokens.length > 2 || inputTokens.length == 0) {
                            writeMessage("error;;The message must be formed in the following way: <command>;;<body>");
                            continue;
                        }
                        commands.get(inputTokens[0]).apply(inputTokens[1], this);
                    } catch (IOException e) {
                        interrupt();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeMessage(String message) {
        writer.println(message);
    }

    public User getUser() {
        return user;
    }

    public void interrupt() {
        try {
            running = false;
            System.out.println("Connection removed: " + socket.getInetAddress().getHostAddress());
            ServerPartyManager.userDisconnected(this);
            LcpServer.remove(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
