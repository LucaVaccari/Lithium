package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class ArtistEntry extends CustomComponent {
    @FXML
    private ImageView proPicImg;
    @FXML
    private Label nameLbl;
    @FXML
    private Label followersLbl;

    private final Artist artist;

    public ArtistEntry(Artist artist) {
        super();
        this.artist = artist;
        initialize();
    }

    private void initialize() {
        var imgBytes = HttpHandler.getBase64Img("/" + artist.getProfilePicturePath());
        Image img = new Image(new ByteArrayInputStream(imgBytes));
        proPicImg.setImage(img);

        nameLbl.setText(artist.getName());

        followersLbl.setText(artist.getFollowerIds().length + " followers");
    }

    public Artist getArtist() {
        return artist;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLS/listComponents/artistEntry.fxml";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistEntry that = (ArtistEntry) o;
        return Objects.equals(artist, that.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist);
    }
}
