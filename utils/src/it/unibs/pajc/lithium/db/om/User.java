package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.ManyToMany;
import it.unibs.pajc.db.Table;

import java.io.Serializable;

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

    @ManyToMany(otherTableName = "user_saved_album", otherTableColumnName = "album_id")
    private Integer[] savedAlbumsIds;
    @ManyToMany(otherTableName = "user_saved_playlist", otherTableColumnName = "playlist_id")
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

}
