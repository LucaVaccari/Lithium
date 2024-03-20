package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.ManyToMany;
import it.unibs.pajc.db.Table;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "artist")
public class Artist implements Serializable {
    @Id
    @Column(name = "artist_id")
    private Integer id;
    @Column(name = "artist_name")
    private String name;
    @Column(name = "artist_bio")
    private String bio;
    @Column(name = "artist_pic_path")
    private String profilePicturePath;
    @ManyToMany(otherTableName = "artist_follower", otherTableColumnName = "user_id")
    private Integer[] followerIds;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public Integer[] getFollowerIds() {
        return followerIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
