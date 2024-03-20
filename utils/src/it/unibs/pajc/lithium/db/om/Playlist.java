package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.ManyToMany;
import it.unibs.pajc.db.Table;

import java.io.Serializable;
import java.util.Objects;

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

    @Column(name = "cover_img_path")
    private String imgPath;

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

    public Integer getOwnerId() {
        return ownerId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public Integer[] getTracksIds() {
        return tracksIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
