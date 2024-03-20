package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.db.om.Artist;
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

        byte[] imgBytes = HttpHandler.getBase64Img("/" + artist.getProfilePicturePath());
        proPicImg.setImage(new Image(new ByteArrayInputStream(imgBytes)));

        // TODO: track container
    }

    public void onBackBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToMainScene();
    }
}
