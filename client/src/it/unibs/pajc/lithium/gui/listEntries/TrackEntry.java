package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class TrackEntry extends CustomComponent {
    @FXML
    private ImageView coverImg;
    @FXML
    private Label titleLbl;
    @FXML
    private Label artistLbl;

    private final Track track;

    public TrackEntry(Track track) {
        super();
        this.track = track;
        initialize();
    }

    private void initialize() {
        var albumJson = HttpHandler.get("/album/" + track.getAlbumId());
        var album = ClientMain.getGson().fromJson(albumJson, Album.class);

        var imgBytes = HttpHandler.getBase64Img("/" + album.getImgPath());
        Image img = new Image(new ByteArrayInputStream(imgBytes));
        coverImg.setImage(img);

        titleLbl.setText(track.getTitle());

        artistLbl.setText("");
        int numberOfArtists = track.getArtistsIds().length;
        if (numberOfArtists == 0) return;
        var artists = new String[numberOfArtists];
        Integer[] artistsIds = album.getArtistsIds();
        for (int i = 0; i < numberOfArtists; i++) {
            var id = artistsIds[i];
            var json = HttpHandler.get("/artist/%d".formatted(id));
            var artist = ClientMain.getGson().fromJson(json, Artist.class);
            artists[i] = artist.getName();
        }
        artistLbl.setText(String.join(", ", artists));
    }

    public Track getTrack() {
        return track;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/trackEntry.fxml";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackEntry that = (TrackEntry) o;
        return Objects.equals(track, that.track);
    }

    @Override
    public int hashCode() {
        return Objects.hash(track);
    }
}
