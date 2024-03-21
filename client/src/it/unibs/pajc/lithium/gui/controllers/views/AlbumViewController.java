package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class AlbumViewController {
    @FXML
    private ImageView coverImg;
    @FXML
    private Label albumTitleLbl;
    @FXML
    private Label artistLbl;
    @FXML
    private Label releaseDateLbl;
    @FXML
    private Label genreLbl;
    @FXML
    private ListView<TrackEntry> trackContainer;
    private Album album;

    @FXML
    private void initialize() {
        album = (Album) MainSceneController.getSelectedItem();
        albumTitleLbl.setText(album.getTitle());

        var artists = ItemProvider.getItems(album.getArtistsIds(), Artist.class);
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        artistLbl.setText(String.join(", ", artistNames));

        releaseDateLbl.setText(album.getReleaseDate());

        // todo genres

        coverImg.setImage(ItemProvider.getImage("/" + album.getImgPath()));

        // TODO: track container
    }

    public void onBackBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToMainScene();
    }

    public void onPlayNowBtn(ActionEvent ignored) {
        // TODO: play album now
    }

    public void onPlayNextBtn(ActionEvent ignored) {
        // TODO: play album next
    }

    public void onAddToQueueBtn(ActionEvent ignored) {
        // TODO: add album to queue
    }
}
