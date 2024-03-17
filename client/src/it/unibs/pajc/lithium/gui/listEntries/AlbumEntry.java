package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class AlbumEntry extends CustomComponent {
    @FXML
    private ImageView coverImg;
    @FXML
    private Label titleLbl;
    @FXML
    private Label artistLbl;

    private final Album album;

    public AlbumEntry(Album album) {
        super();
        this.album = album;
        initialize();
    }

    private void initialize() {
        // TODO get cover img
        titleLbl.setText(album.getTitle());
        // TODO artistsLbl
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/albumEntry.fxml";
    }

    public Album getAlbum() {
        return album;
    }
}
