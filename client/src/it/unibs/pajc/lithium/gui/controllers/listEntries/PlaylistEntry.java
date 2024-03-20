package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.HttpHelper;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class PlaylistEntry extends CustomComponent {
    @FXML
    private Node root;
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
        root.setOnMouseClicked(e -> {
            MainSceneController.setSelectedItem(playlist);
            SceneManager.loadScene("/FXMLs/itemViews/playlistView.fxml", this, false);
        });
    }

    private void initialize() {
        var imgBytes = HttpHandler.getBase64Img("/" + playlist.getImgPath());
        Image img = new Image(new ByteArrayInputStream(imgBytes));
        coverImg.setImage(img);

        nameLbl.setText(playlist.getName());

        var owner = HttpHelper.getPlaylistOwner(playlist);
        authorLbl.setText("by " + owner.getUsername());

        int numberOfTracks = playlist.getTracksIds().length;
        nTracksLbl.setText(numberOfTracks + (numberOfTracks == 1 ? " track" : " tracks"));
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/playlistEntry.fxml";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistEntry that = (PlaylistEntry) o;
        return Objects.equals(playlist, that.playlist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlist);
    }
}
