package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class PlaylistEntry extends CustomComponent {
    @FXML
    private ImageView coverImg;
    @FXML
    private Label nameLbl;
    @FXML
    private Label authorLbl;
    @FXML
    private Label nTracksLbl;

    private final Playlist playlist;

    public PlaylistEntry(Playlist playlist) {
        super();
        this.playlist = playlist;
        initialize();
    }

    private void initialize() {
        var imgBytes = HttpHandler.getBase64Img("/" + playlist.getImgPath());
        Image img = new Image(new ByteArrayInputStream(imgBytes));
        coverImg.setImage(img);

        nameLbl.setText(playlist.getName());

        var ownerJson = HttpHandler.get("/user/%d".formatted(playlist.getOwnerId()));
        var owner = ClientMain.getGson().fromJson(ownerJson, User.class);
        authorLbl.setText("by " + owner.getUsername());

        int numberOfTracks = playlist.getTracksIds().length;
        nTracksLbl.setText(numberOfTracks + (numberOfTracks == 1 ? " track" : " tracks"));
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/playlistEntry.fxml";
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
