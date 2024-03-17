package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
        // TODO proPicImg
        nameLbl.setText(artist.getName());
        // TODO followersLbl
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLS/listComponents/artistEntry.fxml";
    }

    public Artist getArtist() {
        return artist;
    }
}
