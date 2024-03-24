package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

@Table(name = "track_in_playlist")
public class TrackInPlaylist {
    @Id
    @Column(name = "track_id")
    private Integer trackId;
    @Id
    @Column(name = "playlist_id")
    private Integer playlistId;
    @Column(name = "added_on")
    private String addedOn;

    public TrackInPlaylist(Integer trackId, Integer playlistId, String addedOn) {
        this.trackId = trackId;
        this.playlistId = playlistId;
        this.addedOn = addedOn;
    }
}
