package it.unibs.pajc.lithium.connection;

import it.unibs.pajc.lithium.db.om.Track;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ListeningParty {
    private final Set<Connection> participants = ConcurrentHashMap.newKeySet();
    private Connection ownerConnection;
    private Track currentTrack;
    private double currentTime;

    public ListeningParty(Connection ownerConnection) {
        this.ownerConnection = ownerConnection;
        participants.add(ownerConnection);
    }

    public void join(Connection connection) {
        participants.add(connection);
        connection.writeMessage("partyTrack;;" + currentTrack.getId());
        connection.writeMessage("partySync;;" + currentTime);
    }

    public void leave(Connection connection) {
        participants.remove(connection);
        if (connection.equals(ownerConnection)) {
            Optional<Connection> ownerCandidate = participants.stream().findFirst();
            ownerCandidate.ifPresent(value -> {
                ownerConnection = value;
                broadcast("hostUpdated;;" + ownerConnection.getUser().getId(), connection);
            });
        }
    }

    public void sync(double timestamp, Connection connection) {
        if (timestamp > currentTrack.getDuration() + 2 || timestamp < 0) {
            connection.writeMessage(
                    "error;;The timestamp must be greater than 0 and smaller than the duration of the" + " " + "track");
            return;
        }
        if (!connection.equals(ownerConnection)) {
            connection.writeMessage("error;;Only the owner of the party can change the timestamp");
            return;
        }
        currentTime = timestamp;
        broadcast("partySync;;" + timestamp, connection);
    }

    public void updateTrack(Track track, Connection connection) {
        if (!connection.equals(ownerConnection)) {
            connection.writeMessage("error;;Only the owner of the party can change the track");
            return;
        }
        currentTrack = track;
        broadcast("partyTrack;;" + track.getId(), connection);
        sync(0, connection);
    }

    public void broadcast(String message, Connection sender) {
        if (participants.contains(sender)) {
            sender.writeMessage("error;;You are not part of this party");
            return;
        }
        participants.stream().filter(p -> !p.equals(sender)).forEach(p -> p.writeMessage(message));
    }

    public boolean isEmpty() {
        return participants.isEmpty();
    }
}
