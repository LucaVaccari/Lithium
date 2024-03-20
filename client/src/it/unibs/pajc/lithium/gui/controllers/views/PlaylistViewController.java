package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PlaylistViewController {
    private Playlist playlist;

    @FXML
    private void initialize() {
        playlist = (Playlist) MainSceneController.getSelectedItem();
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
