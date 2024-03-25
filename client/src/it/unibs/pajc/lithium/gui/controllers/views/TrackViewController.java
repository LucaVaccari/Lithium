package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.PlaybackManager;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.PlaybackController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TrackViewController {
    // TODO: make album clickable
    // TODO: make artist clickable

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
    @FXML
    private PlaybackController playbackController;
    private Track track;
    private Album album;

    @FXML
    private void initialize() {
        track = (Track) MainSceneController.getSelectedItem();
        album = ItemProvider.getItem(track.getAlbumId(), Album.class);
        trackTitleLbl.setText(track.getTitle());
        trackAlbumLbl.setText(album.getTitle());

        artistLbl.setText(ItemProvider.getArtistNamesFormatted(track.getArtistIds()));

        releaseDateLbl.setText("Released on " + album.getReleaseDate());
        genreListLbl.setText(ItemProvider.getGenresFormatted(track.getGenreIds()));
        coverImg.setImage(ItemProvider.getImage(album.getImgPath()));
    }

    public void onAlbumMouseClicked(MouseEvent ignored) {
        MainSceneController.setSelectedItem(album);
        SceneManager.loadScene("/FXMLs/itemViews/albumView.fxml", this);
    }
    // todo: on artist clicked

    // todo: on genre clicked

    public void onPlayNowBtn(ActionEvent ignored) {
        PlaybackManager.playImmediately(track);
    }

    public void onPlayNextBtn(ActionEvent ignored) {
        PlaybackManager.playNext(track);
    }

    public void onAddToQueueBtn(ActionEvent ignored) {
        PlaybackManager.addToQueue(track);
    }

    public void addToPlaylist(ActionEvent ignored) {
        MainSceneController.setSelectedItem(track);
        SceneManager.openBlockingWindow("Track adding", "/FXMLs/trackInPlaylist.fxml", this);
    }
}
