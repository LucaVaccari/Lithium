package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PlaylistEntry extends CustomComponent {
    @FXML
    private ImageView coverImg;
    @FXML
    private Label nameLbl;
    @FXML
    private Label authorLbl;

    private final Playlist playlist;

    public PlaylistEntry(Playlist playlist) {
        super();
        this.playlist = playlist;
        initialize();
    }

    private void initialize() {
        // TODO coverImg
        nameLbl.setText(playlist.getName());
        // TODO authorLbl
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/playlistEntry.fxml";
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
