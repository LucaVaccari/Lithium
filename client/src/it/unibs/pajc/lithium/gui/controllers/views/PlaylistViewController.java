package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.HttpHelper;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class PlaylistViewController {
    @FXML
    private Label playlistNameLbl;
    @FXML
    private Label ownerLlb;
    @FXML
    private Label creationDateLbl;
    @FXML
    private Label genresLbl;
    @FXML
    private ImageView coverImg;
    @FXML
    public ListView<TrackEntry> trackContainer;
    private Playlist playlist;

    @FXML
    private void initialize() {
        playlist = (Playlist) MainSceneController.getSelectedItem();
        var owner = HttpHelper.getPlaylistOwner(playlist);

        playlistNameLbl.setText(playlist.getName());
        ownerLlb.setText("By " + owner.getUsername());

        // TODO: creation date
        // TODO: genres

        coverImg.setImage(new Image(new ByteArrayInputStream(HttpHandler.getBase64Img("/" + playlist.getImgPath()))));

        // TODO: fill track container
    }

    public void onBackBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToMainScene();
    }

    public void onPlayNowBtn(ActionEvent ignored) {
        // TODO: play playlist now
    }

    public void onPlayNextBtn(ActionEvent ignored) {
        // TODO: play playlist next
    }

    public void onAddToQueueBtn(ActionEvent ignored) {
        // TODO: add playlist to queue
    }
}
