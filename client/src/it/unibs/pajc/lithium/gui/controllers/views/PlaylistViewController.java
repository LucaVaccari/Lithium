package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.PlaybackManager;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.PlaybackController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

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
    @FXML
    private PlaybackController playbackController;
    private Track[] tracks;

    @FXML
    private void initialize() {
        var playlist = (Playlist) MainSceneController.getSelectedItem();
        var owner = ItemProvider.getItem(playlist.getOwnerId(), User.class);

        playlistNameLbl.setText(playlist.getName());
        ownerLlb.setText("By " + owner.getUsername());

        // TODO: creation date
        // TODO: genres

        coverImg.setImage(ItemProvider.getImage(playlist.getImgPath()));

        tracks = ItemProvider.getItems(playlist.getTracksIds(), Track.class);
        trackContainer.getItems().clear();
        for (var track : tracks) {
            trackContainer.getItems().add(new TrackEntry(track));
        }
    }

    public void onBackBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToPreviousScene();
    }

    public void onPlayNowBtn(ActionEvent ignored) {
        PlaybackManager.playImmediately(tracks);
    }

    public void onPlayNextBtn(ActionEvent ignored) {
        PlaybackManager.playNext(tracks);
    }

    public void onAddToQueueBtn(ActionEvent ignored) {
        PlaybackManager.addToQueue(tracks);
    }
}
