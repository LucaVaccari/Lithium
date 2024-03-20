package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.HttpHelper;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class TrackViewController {
    @FXML
    private Label trackTitleLbl;
    @FXML
    private Label trackAlbumLbl;
    @FXML
    private Label artistLbl;
    @FXML
    private Label releaseDateLbl;
    @FXML
    private Label genreListLbl;
    @FXML
    private ImageView coverImg;
    private Track track;

    @FXML
    private void initialize() {
        track = (Track) MainSceneController.getSelectedItem();
        var album = ClientMain.getGson().fromJson(HttpHandler.get("/album/" + track.getAlbumId()), Album.class);
        trackTitleLbl.setText(track.getTitle());
        trackAlbumLbl.setText(album.getTitle());

        var artists = HttpHelper.getArtists(album.getArtistsIds());
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        artistLbl.setText(String.join(", ", artistNames));

        releaseDateLbl.setText("Released on " + album.getReleaseDate());
        // todo genre list
        coverImg.setImage(new Image(new ByteArrayInputStream(HttpHandler.getBase64Img("/" + album.getImgPath()))));
    }

    public void onBackBtn() {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToMainScene();
    }

    public void onPlayNowBtn(ActionEvent ignored) {
        // TODO: play track now
    }

    public void onPlayNextBtn(ActionEvent ignored) {
        // TODO: play track next
    }

    public void onAddToQueueBtn(ActionEvent ignored) {
        // TODO: add track to queue
    }
}
