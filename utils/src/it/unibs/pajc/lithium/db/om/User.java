package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.ForeignKey;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "user")
public class User implements Serializable {
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
    private Integer[] savedAlbumsIds;
    @ForeignKey(otherTableName = "user_saved_playlist", otherTableColumnName = "playlist_id")
    private Integer[] savedPlaylistsIds;

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
