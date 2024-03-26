package it.unibs.pajc.lithium.connection;

import it.unibs.pajc.lithium.ServerMain;
import it.unibs.pajc.lithium.db.om.Track;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles all the parties active in the server
 */
public class ServerPartyManager {
    private static final Map<Integer, ListeningParty> parties = new ConcurrentHashMap<>();
    private static final Stack<Integer> unusedIds = new Stack<>();
    private static int lastId = 0;

    public static void createParty(LcpConnection ownerConnection) {
        if (notAuth(ownerConnection)) return;
        var id = unusedIds.isEmpty() ? lastId++ : unusedIds.pop();
        var party = new ListeningParty(ownerConnection);
        parties.put(id, party);
    }

    /**
     * Joins a party. If there's no party with the id in the body, a new one is created
     *
     * @param body       Must contain either the id of the party to join or the keyword 'new' to create a new one
     * @param connection The connection from which this message arrived
     */
    public static void joinParty(String body, LcpConnection connection) {
        if (notAuth(connection)) return;
        if (body.equalsIgnoreCase("new")) {
            createParty(connection);
            return;
        }
        var partyId = Integer.parseInt(body);
        if (partyNotExists(partyId, connection)) return;
        parties.get(partyId).join(connection);
    }

    /**
     * Leaves a party. If the connection is the last one of the party, the party is then deleted.
     *
     * @param body       The id of the party.
     * @param connection The connection from which this message arrived
     */
    public static void leaveParty(String body, LcpConnection connection) {
        if (notAuth(connection)) return;
        var partyId = Integer.parseInt(body);
        if (partyNotExists(partyId, connection)) return;
        parties.get(partyId).leave(connection);
        if (parties.get(partyId).isEmpty()) {
            parties.remove(partyId);
            unusedIds.add(partyId);
        }
    }

    /**
     * Syncs the party playback
     *
     * @param body       [partyId]::[timestamp] (partyId int, timestamp double)
     * @param connection The connection from which this message arrived
     */
    public static void syncParty(String body, LcpConnection connection) {
        if (notAuth(connection)) return;
        var tokens = body.strip().split("::");
        if (tokens.length != 2) {
            connection.writeMessage(
                    "error;;The body of the message must be formed in the following way: " + "<partyId>::<timestamp>");
            return;
        }
        var partyId = Integer.parseInt(tokens[0]);
        if (partyNotExists(partyId, connection)) return;
        var timestamp = Double.parseDouble(tokens[1]);
        parties.get(partyId).sync(timestamp, connection);
    }

    /**
     * Updates the currently playing track of a party
     *
     * @param body       [partyId]::[trackId]
     * @param connection The connection from which this message arrived
     */
    public static void updateTrack(String body, LcpConnection connection) {
        if (notAuth(connection)) return;
        var tokens = body.strip().split("::");
        if (tokens.length != 2) {
            connection.writeMessage(
                    "error;;The body of the message must be formed in the following way: " + "<partyId>::<trackId>");
            return;
        }
        var partyId = Integer.parseInt(tokens[0]);
        if (partyNotExists(partyId, connection)) return;
        var trackId = Integer.parseInt(tokens[1]);
        var track = ServerMain.getDbConnector().getObjectById(trackId, Track.class);
        if (track == null) {
            connection.writeMessage("error;;There's no track with id " + trackId);
            return;
        }
        parties.get(partyId).updateTrack(track, connection);
    }

    /**
     * Sends a chat message to all the users in the party, except for the message sender
     *
     * @param body       [partyId]::[message]
     * @param connection The connection from which this message arrived
     */
    public static void chat(String body, LcpConnection connection) {
        if (notAuth(connection)) return;
        var tokens = body.strip().split("::");
        if (tokens.length != 2) {
            connection.writeMessage(
                    "error;;The body of the message must be formed in the following way: " + "<partyId>::<message>");
            return;
        }
        var partyId = Integer.parseInt(tokens[0]);
        if (partyNotExists(partyId, connection)) return;
        parties.get(partyId).broadcast(tokens[1], connection);
    }

    public static void userDisconnected(LcpConnection connection) {
        for (var id : parties.keySet()) {
            parties.get(id).leave(connection);
        }
    }

    public static void allParties(String ignored, LcpConnection connection) {
        var body = String.join("::", parties.keySet().stream().map(String::valueOf).toList());
        connection.writeMessage("allParties;;" + body);
    }

    /**
     * Utility to check if the user is not authenticated
     *
     * @param connection The connection from which a message arrived
     * @return true if the user is not authenticated, false otherwise
     */
    private static boolean notAuth(LcpConnection connection) {
        if (connection.getUser() == null) {
            connection.writeMessage("error;;The user is not authenticated");
            return true;
        }
        return false;
    }

    /**
     * Utility to check if the party does not exist
     *
     * @param partyId    The id of the party to check
     * @param connection The connection from which a message arrived
     * @return true if the party does not exist, false otherwise
     */
    private static boolean partyNotExists(int partyId, LcpConnection connection) {
        if (!parties.containsKey(partyId)) {
            connection.writeMessage("error;;The specified party does not exist");
            return true;
        }
        return false;
    }
}
