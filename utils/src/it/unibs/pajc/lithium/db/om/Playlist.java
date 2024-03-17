package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.ManyToMany;
import it.unibs.pajc.db.Table;

import java.io.Serializable;

@Table(name = "playlist")
public class Playlist implements Serializable {
    @Id
    @Column(name = "playlist_id")
    private Integer id;

    @Column(name = "playlist_title")
    private String name;

    @Column(name = "playlist_description")
    private String description;

    @Column(name = "user_id")
    private Integer ownerId;

    @ManyToMany(otherTableName = "track_in_playlist", otherTableColumnName = "track_id")
    private Integer[] tracksIds;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
