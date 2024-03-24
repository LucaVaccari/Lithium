package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Table;

@Table(name = "user_saved_album")
public class UserSavedAlbum {
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "album_id")
    private Integer albumId;

    public UserSavedAlbum(Integer userId, Integer albumId) {
        this.userId = userId;
        this.albumId = albumId;
    }
}
