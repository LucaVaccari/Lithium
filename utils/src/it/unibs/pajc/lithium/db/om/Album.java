package it.unibs.pajc.lithium.db.om;


import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.ForeignKey;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Model for the table 'album' on the db.
 */
@Table(name = "album")
public class Album implements Item, Serializable {
    @Id
    @Column(name = "album_id")
    private Integer id;
    @Column(name = "album_title")
    private String title;
    @Column(name = "album_version")
    private String version;
    @Column(name = "album_release_date")
    private String releaseDate;
    @Column(name = "cover_img_path")
    private String imgPath;
    @ForeignKey(otherTableName = "album_by_artist", otherTableColumnName = "artist_id")
    private Integer[] artistsIds;
    @ForeignKey(otherTableName = "album_genre", otherTableColumnName = "genre_id")
    private Integer[] genresIds;
    @ForeignKey(otherTableName = "track", otherTableColumnName = "track_id")
    private Integer[] trackIds;

    public Album() {
    }

    public Album(String title, String version, String releaseDate, String imgPath, Integer[] artists,
                 Integer[] genres) {
        this.title = title;
        this.version = version;
        this.releaseDate = releaseDate;
        this.imgPath = imgPath;
        this.artistsIds = artists;
        this.genresIds = genres;
    }

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImgPath() {
        return imgPath;
    }

    public Integer[] getArtistsIds() {
        return artistsIds;
    }

    public Integer[] getTrackIds() {
        return trackIds;
    }

    @Override
    public String toString() {
        return "{id=%d, title='%s', version='%s', releaseDate='%s', imgPath='%s', artists='%s'}".formatted(id, title,
                version, releaseDate, imgPath, Arrays.toString(artistsIds));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
