package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ArtistViewController {
    private Artist artist;

    @FXML
    private void initialize() {
        artist = (Artist) MainSceneController.getSelectedItem();
    }

    public void onBackBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToMainScene();
    }
}
