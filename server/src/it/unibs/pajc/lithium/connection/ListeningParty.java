package it.unibs.pajc.lithium.connection;

import it.unibs.pajc.lithium.Logger;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ListeningParty {
    private final Set<LcpConnection> participants = ConcurrentHashMap.newKeySet();
    private LcpConnection hostConnection;
    private Track currentTrack;
    private double currentTime;
    private boolean paused;

    public ListeningParty(LcpConnection hostConnection) {
        this.hostConnection = hostConnection;
        participants.add(hostConnection);
        sendUserUpdate();
        hostConnection.writeMessage("host;;" + hostConnection.getUser().getId());
    }

    public void join(LcpConnection connection) {
        participants.add(connection);
        if (currentTrack != null) connection.writeMessage("partyTrack;;" + currentTrack.getId());
        connection.writeMessage("partySync;;" + currentTime);
        sendUserUpdate();
        connection.writeMessage("host;;" + hostConnection.getUser().getId());
        connection.writeMessage("pause;;" + (paused ? "pause" : "unpause"));
    }

    public void leave(LcpConnection connection) {
        if (participants.contains(connection)) {
            participants.remove(connection);
            sendUserUpdate();
        }
        if (connection.equals(hostConnection)) {
            Optional<LcpConnection> ownerCandidate = participants.stream().findFirst();
            ownerCandidate.ifPresent(value -> {
                hostConnection = value;
                broadcast("host;;" + value.getUser().getId(), connection, true);
            });
        }
    }

    public void sync(double timestamp, LcpConnection connection) {
        if (currentTrack == null) {
            Logger.log("Track is null, ignoring the sync request");
            return;
        }
        if (timestamp > currentTrack.getDuration() + 2 || timestamp < 0) {
            connection.writeMessage(
                    "error;;The timestamp must be greater than 0 and smaller than the duration of the track");
            return;
        }
        if (!connection.equals(hostConnection)) {
            connection.writeMessage("error;;Only the owner of the party can change the timestamp");
            return;
        }
        currentTime = timestamp;
        broadcast("partySync;;" + timestamp, connection, true);
    }

    public void updateTrack(Track track, LcpConnection connection) {
        if (!connection.equals(hostConnection)) {
            connection.writeMessage("error;;Only the owner of the party can change the track");
            return;
        }
        currentTrack = track;
        broadcast("partyTrack;;" + track.getId(), connection, true);
        sync(0, connection);
    }

    public void pause(boolean pause, LcpConnection connection) {
        this.paused = pause;
        broadcast("pause;;" + (pause ? "pause" : "unpause"), connection, true);
    }

    public void broadcast(String message, LcpConnection sender, boolean ignoreSender) {
        participants.stream().filter(p -> !p.equals(sender) && ignoreSender).forEach(p -> p.writeMessage(message));
    }

    private void sendUserUpdate() {
        var body = String.join("::", participants.stream().map(p -> String.valueOf(p.getUser().getId())).toList());
        participants.forEach(p -> p.writeMessage("userUpdate;;" + body));
    }

    public boolean isEmpty() {
        return participants.isEmpty();
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public User getOwner() {
        return hostConnection.getUser();
    }
}
