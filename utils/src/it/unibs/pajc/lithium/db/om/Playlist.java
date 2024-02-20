package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

@Table(name = "playlist")
public class Playlist {
    @Id
    @Column(name = "playlist_id")
    private Integer id;

    @Column(name = "playlist_title")
    private String title;

    @Column(name = "playlist_description")
    private String description;

    private Track[] tracks;
    private User owner;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
