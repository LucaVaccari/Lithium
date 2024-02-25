package it.unibs.pajc.lithium.db.om;


import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Table;
import it.unibs.pajc.db.Id;

import java.sql.Date;

/**
 * Model for the table 'album' on the db.
 */
@Table(name = "album")
public class Album {
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

    private Artist[] artists;
    private Genre[] genres;

    public Album() {
    }

    public Album(String title, String version, String releaseDate, String imgPath, Artist[] artists, Genre[] genres) {
        this.title = title;
        this.version = version;
        this.releaseDate = releaseDate;
        this.imgPath = imgPath;
        this.artists = artists;
        this.genres = genres;
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

    @Override
    public String toString() {
        return "{" + "id=" + id + ", title='" + title + '\'' + ", version='" + version + '\'' + ", releaseDate='" + releaseDate + '\'' + ", imgPath='" + imgPath + '\'' + '}';
    }
}
