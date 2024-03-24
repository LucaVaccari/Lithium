package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Table;

@Table(name = "artist_follower")
public class ArtistFollower {
    @Column(name = "artist_id")
    private Integer artistId;
    @Column(name = "user_id")
    private Integer userId;

    public ArtistFollower(Integer artistId, Integer userId) {
        this.artistId = artistId;
        this.userId = userId;
    }
}
