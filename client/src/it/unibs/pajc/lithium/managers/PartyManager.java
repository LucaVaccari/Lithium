package it.unibs.pajc.lithium.managers;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.util.Observer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static it.unibs.pajc.lithium.ClientMain.getConnectionManager;

/**
 * Provides methods for sending/receiving messages from the server. Most of the methods work differently depending
 * on the state of the application.
 */
public class PartyManager {
    private static int id = -1;
    private static boolean host = false;
    private static final Set<User> participants = new HashSet<>();

    /**
     * Event invoked when the participants of a party are updated. Contains the list of users members of the party
     */
    public static final Observer<Set<User>> participantsUpdate = new Observer<>();
    /**
     * Event invoked when the list of all parties changes.
     * Contains a list of parties, each represented by three ids: the id of the party, the id of the track currently
     * playing and the id of the user host.
     */
    public static final Observer<Set<Integer[]>> partiesUpdate = new Observer<>();
    /**
     * Event invoked when a chat message is received. Contains the message received.
     */
    public static final Observer<String> messageReceived = new Observer<>();
    /**
     * Event invoked when the host of the active party has changed.
     */
    public static final Observer<Integer> hostUpdate = new Observer<>();

    /**
     * Event invoked when a party is joined or left (in the last case, the id is -1).
     * Contains the id of the party (or -1).
     */
    public static final Observer<Integer> partyJoined = new Observer<>();

    public static void requestAllParties() {
        getConnectionManager().writeMessage("allParties;;ignored");
    }

    public static void partiesUpdate(String body) {
        if (body.equals("null")) return;
        var parties = Arrays.stream(body.strip().split("::"))
                .map(e -> Arrays.stream(e.strip().split(",,")).map(Integer::parseInt).toArray(Integer[]::new)).toList();
        partiesUpdate.invoke(Set.copyOf(parties));
    }

    /**
     * Send only.
     * Sends a request to create a new party.
     */
    public static void createParty() {
        if (anyPartyJoined()) leaveParty();
        host = true;
        getConnectionManager().writeMessage("joinParty;;new");
    }

    /**
     * Send only. Sends a request to join an existing party.
     *
     * @param id The id of the party to join.
     */
    public static void joinParty(int id) {
        if (anyPartyJoined()) leaveParty();
        PartyManager.id = id;
        getConnectionManager().writeMessage("joinParty;;" + id);
    }

    /**
     * Sends a request to leave a party.
     */
    public static void leaveParty() {
        if (anyPartyJoined()) {
            getConnectionManager().writeMessage("leaveParty;;" + id);
            setId(-1);
        }
    }

    /**
     * Sends/receives (based on app state) a request to sync the playback.
     *
     * @param timestamp The current playback time
     */
    public static void syncParty(double timestamp) {
        if (!anyPartyJoined()) return;
        if (host) getConnectionManager().writeMessage("syncParty;;%d::%f".formatted(id, timestamp));
        else PlaybackManager.seek(timestamp);
    }

    /**
     * Sends/receives (based on app state) a request to set a new track
     *
     * @param track The new track
     */
    public static void setCurrentTrack(Track track) {
        if (!anyPartyJoined()) return;
        if (host) getConnectionManager().writeMessage("partyTrack;;%d::%d".formatted(id, track.getId()));
        else PlaybackManager.playImmediately(track);
    }

    /**
     * Sends/receives (based on app state a request to play/pause the playback
     *
     * @param pause true for pausing the playback, false for resuming it
     */
    public static void pause(boolean pause) {
        if (!anyPartyJoined()) return;
        if (host) getConnectionManager().writeMessage("pause;;" + (pause ? "pause" : "unpause"));
        else if (pause) PlaybackManager.pause();
        else PlaybackManager.play();
    }

    /**
     * Sends a chat message to the other party users
     *
     * @param message The chat message
     */
    public static void sendPartyChat(String message) {
        if (!anyPartyJoined()) return;
        String safeMessage = message.strip().replaceAll(";+", ";").replaceAll(":+", ":");
        getConnectionManager().writeMessage("partyChat;;%d::%s".formatted(id, safeMessage));
    }

    /**
     * Receives a chat message from the party
     *
     * @param message The message received
     */
    public static void partyChatReceived(String message) {
        messageReceived.invoke(message);
    }

    public static void userUpdate(String body) {
        participants.clear();
        var tokens = body.strip().split("::");
        for (String token : tokens) {
            var user = ItemProvider.getItem(Integer.parseInt(token), User.class);
            if (user == null) throw new IllegalArgumentException("A user member of the party is null ");
            participants.add(user);
        }
        participantsUpdate.invoke(Collections.unmodifiableSet(participants));
    }

    public static void updateHost(int hostId) {
        host = AccountManager.getUser().getId() == hostId;
        hostUpdate.invoke(hostId);
    }

    public static boolean anyPartyJoined() {
        return id != -1;
    }

    public static void setId(int id) {
        PartyManager.id = id;
        partyJoined.invoke(id);
    }

    public static int getId() {
        return id;
    }

    public static boolean isHost() {
        return host;
    }
}
