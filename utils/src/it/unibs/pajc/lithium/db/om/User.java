package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.ForeignKey;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Table(name = "user")
public class User implements Item, Serializable {
    @Id
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "pwd_hash")
    private String pwdHash;
    @Column(name = "profile_pic_path")
    private String profilePicPath;
    @ForeignKey(otherTableName = "user_saved_album", otherTableColumnName = "album_id")
    private Integer[] savedAlbumIds;
    @ForeignKey(otherTableName = "user_saved_playlist", otherTableColumnName = "playlist_id")
    private Integer[] savedPlaylistIds;
    @ForeignKey(otherTableName = "artist_follower", otherTableColumnName = "artist_id")
    private Integer[] followedArtistIds;

    public User() {
    }

    public User(String username, String pwdHash) {
        this.username = username;
        this.pwdHash = pwdHash;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public Integer[] getSavedAlbumIds() {
        return savedAlbumIds;
    }

    public Integer[] getSavedPlaylistIds() {
        return savedPlaylistIds;
    }

    public Integer[] getFollowedArtistIds() {
        return followedArtistIds;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", pwdHash='" + pwdHash + '\'' +
                ", profilePicPath='" + profilePicPath + '\'' + ", savedAlbumIds=" + Arrays.toString(savedAlbumIds) +
                ", savedPlaylistIds=" + Arrays.toString(savedPlaylistIds) + ", followedArtistIds=" +
                Arrays.toString(followedArtistIds) + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
