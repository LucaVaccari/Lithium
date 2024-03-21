package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class ArtistViewController {
    @FXML
    private Label artistNameLbl;
    @FXML
    private Label bioLbl;
    @FXML
    private Label genreLbl;
    @FXML
    private ImageView proPicImg;
    @FXML
    private ListView<TrackEntry> trackContainer;
    private Artist artist;

    @FXML
    private void initialize() {
        artist = (Artist) MainSceneController.getSelectedItem();
        artistNameLbl.setText(artist.getName());
        bioLbl.setText(artist.getBio());

        // TODO genres

        proPicImg.setImage(ItemProvider.getImage("/" + artist.getProfilePicturePath()));

        // TODO: track container
    }

    public void onBackBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToPreviousScene();
    }
}
