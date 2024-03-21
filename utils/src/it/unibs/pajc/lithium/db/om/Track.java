package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.ForeignKey;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "track")
public class Track implements Serializable {
    @Id
    @Column(name = "track_id")
    private Integer id;

    @Column(name = "track_title")
    private String title;

    @Column(name = "track_version")
    private String version;

    @Column(name = "track_number")
    private int number;

    @Column(name = "duration")
    private int duration;

    @Column(name = "audio_path")
    private String audioPath;

    @Column(name = "album_id")
    private Integer albumId;

    @ForeignKey(otherTableName = "track_by_artist", otherTableColumnName = "artist_id")
    private Integer[] artistsIds;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public Integer[] getArtistsIds() {
        return artistsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Objects.equals(id, track.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
