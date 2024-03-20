package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.HttpHelper;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
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
    private Album album;

    @FXML
    private void initialize() {
        album = (Album) MainSceneController.getSelectedItem();
        albumTitleLbl.setText(album.getTitle());

        var artists = HttpHelper.getArtists(album.getArtistsIds());
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        artistLbl.setText(String.join(", ", artistNames));

        releaseDateLbl.setText(album.getReleaseDate());

        // todo genres

        coverImg.setImage(new Image(new ByteArrayInputStream(HttpHandler.getBase64Img("/" + album.getImgPath()))));
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
