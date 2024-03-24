package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.*;

@Table(name = "user_saved_playlist")
public class UserSavedPlaylist {
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "playlist_id")
    private Integer playlistId;

    public UserSavedPlaylist(Integer userId, Integer playlistId) {
        this.userId = userId;
        this.playlistId = playlistId;
    }
}
