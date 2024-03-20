package it.unibs.pajc.lithium.db.om;


import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.ManyToMany;
import it.unibs.pajc.db.Table;
import it.unibs.pajc.db.Id;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;

/**
 * Model for the table 'album' on the db.
 */
@Table(name = "album")
public class Album implements Serializable {
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
    @ManyToMany(otherTableName = "album_by_artist", otherTableColumnName = "artist_id")
    private Integer[] artistsIds;
    @ManyToMany(otherTableName = "album_genre", otherTableColumnName = "genre_id")
    private Integer[] genresIds;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getReleaseDate() {
        return Date.valueOf(releaseDate);
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate.toString();
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer[] getGenresIds() {
        return genresIds;
    }

    public Integer[] getArtistsIds() {
        return artistsIds;
    }

    @Override
    public String toString() {
        return "{id=%d, title='%s', version='%s', releaseDate='%s', imgPath='%s', artists='%s'}".formatted(id, title,
                version, releaseDate, imgPath, Arrays.toString(artistsIds));
    }
}
