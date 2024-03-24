package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

@Table(name = "user_saved_playlist")
public class UserSavedPlaylist {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Id
    @Column(name = "playlist_id")
    private Integer playlistId;

    public UserSavedPlaylist(Integer userId, Integer playlistId) {
        this.userId = userId;
        this.playlistId = playlistId;
    }
}
