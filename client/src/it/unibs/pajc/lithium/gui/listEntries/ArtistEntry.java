package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

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

    @Override
    protected String fxmlPath() {
        return "/FXMLS/listComponents/artistEntry.fxml";
    }

    public Artist getArtist() {
        return artist;
    }
}
