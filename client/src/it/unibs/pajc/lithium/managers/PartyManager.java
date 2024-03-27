package it.unibs.pajc.lithium.managers;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.util.Observer;
import it.unibs.pajc.util.Observer2;

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
    private static int hostId = -1;
    private static boolean isHost = false;
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
    public static final Observer2<Integer, String> messageReceived = new Observer2<>();
    /**
     * Event invoked when the host of the active party has changed.
     * Contains the id of the new host
     */
    public static final Observer<Integer> hostUpdate = new Observer<>();

    /**
     * Event invoked when a party is joined or left (in the last case, the id is -1).
     * Contains the id of the party (or -1).
     */
    public static final Observer<Integer> partyJoined = new Observer<>();

    static {
        new Thread(() -> {
            while (true) {
                if (joinedAndHost()) {
                    sendSync(PlaybackManager.getCurrentTime());
                    sendPause(!PlaybackManager.isPlaying());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void requestAllParties() {
        getConnectionManager().writeMessage("allParties;;ignored");
    }

    public static void partiesUpdate(String body) {
        if (body.equals("null")) {
            partiesUpdate.invoke(new HashSet<>());
            return;
        }
        var parties = Arrays.stream(body.strip().split("::"))
                .map(e -> Arrays.stream(e.strip().split(",,")).map(Integer::parseInt).toArray(Integer[]::new)).toList();
        partiesUpdate.invoke(Set.copyOf(parties));
    }

    /**
     * Send only.
     * Sends a request to create a new party.
     */
    public static void sendCreateParty() {
        if (anyPartyJoined()) sendLeave();
        isHost = true;
        getConnectionManager().writeMessage("joinParty;;new");
        if (PlaybackManager.getCurentTrack() != null) {
            sendCurrentTrack(PlaybackManager.getCurentTrack());
            sendPause(false);
        }
        sendSync(PlaybackManager.getCurrentTime());
    }

    /**
     * Sends a request to join an existing party.
     *
     * @param id The id of the party to join.
     */
    public static void sendJoin(int id) {
        if (anyPartyJoined()) sendLeave();
        setId(id);
        getConnectionManager().writeMessage("joinParty;;" + id);
    }

    /**
     * Sends a request to leave a party.
     */
    public static void sendLeave() {
        if (anyPartyJoined()) {
            getConnectionManager().writeMessage("leaveParty;;" + id);
            setId(-1);
        }
    }

    /**
     * Sends a request to sync the playback.
     *
     * @param timestamp The current playback time
     */
    public static void sendSync(double timestamp) {
        if (!anyPartyJoined()) return;
        if (isHost) getConnectionManager().writeMessage("syncParty;;%d::%f".formatted(id, timestamp));
    }

    /**
     * Receives a request to sync the playback.
     *
     * @param timestamp The current playback time in seconds
     */
    public static void receiveSync(double timestamp) {
        if (!anyPartyJoined()) return;
        if (!isHost && Math.abs(timestamp - PlaybackManager.getCurrentTime()) > 1) PlaybackManager.seek(timestamp);
    }

    /**
     * Sends a request to set a new track
     *
     * @param track The new track
     */
    public static void sendCurrentTrack(Track track) {
        if (!anyPartyJoined()) return;
        if (isHost) getConnectionManager().writeMessage("partyTrack;;%d::%d".formatted(id, track.getId()));
    }

    /**
     * Receives a request to set a new track
     *
     * @param track The new track
     */
    public static void receiveCurrentTrack(Track track) {
        if (!anyPartyJoined()) return;
        if (!isHost) PlaybackManager.playImmediately(track);
    }

    /**
     * Sends a request to play/pause the playback
     *
     * @param pause true for pausing the playback, false for resuming it
     */
    public static void sendPause(boolean pause) {
        if (!anyPartyJoined()) return;
        if (isHost) getConnectionManager().writeMessage("pause;;%d::%s".formatted(id, pause ? "pause" : "unpause"));
    }

    /**
     * Receives a request to play/pause the playback
     *
     * @param pause true for pausing the playback, false for resuming it
     */
    public static void receivePause(boolean pause) {
        if (!anyPartyJoined() || isHost) return;
        System.out.println("Pause received");
        if (pause) PlaybackManager.pause();
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
        if (!anyPartyJoined()) return;
        var tokens = message.strip().split("::");
        messageReceived.invoke(Integer.parseInt(tokens[0]), tokens[1]);
    }

    public static void receiveUserUpdate(String body) {
        participants.clear();
        var tokens = body.strip().split("::");
        for (String token : tokens) {
            var user = ItemProvider.getItem(Integer.parseInt(token), User.class);
            if (user == null) throw new IllegalArgumentException("A user member of the party is null ");
            participants.add(user);
        }
        participantsUpdate.invoke(Collections.unmodifiableSet(participants));
    }

    public static void receiveHostUpdate(int hostId) {
        isHost = AccountManager.getUser().getId() == hostId;
        PartyManager.hostId = hostId;
        hostUpdate.invoke(hostId);
    }

    public static boolean anyPartyJoined() {
        return id != -1;
    }

    public static void setId(int id) {
        PartyManager.id = id;
        if (PlaybackManager.getCurentTrack() != null && isHost) {
            sendCurrentTrack(PlaybackManager.getCurentTrack());
            sendPause(false);
        }
        partyJoined.invoke(id);
    }

    public static int getId() {
        return id;
    }

    public static boolean isHost() {
        return isHost;
    }

    public static int getHostId() {
        return hostId;
    }

    public static boolean joinedAndHost() {
        return anyPartyJoined() && isHost();
    }
}
