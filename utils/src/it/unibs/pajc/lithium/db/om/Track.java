package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

@Table(name = "track")
public class Track {
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

    private Artist[] artists;

    public Integer getId() {
        return id;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}