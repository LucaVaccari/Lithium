package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.AccountManager;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.PlaybackManager;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.GUIUtils;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.PlaybackController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class PlaylistViewController {
    @FXML
    private Label playlistNameLbl;
    @FXML
    private Label ownerLlb;
    @FXML
    private Label creationDateLbl;
    @FXML
    private Label genreLbl;
    @FXML
    private Button saveBtn;
    @FXML
    private Button manageBtn;
    @FXML
    public ListView<TrackEntry> trackContainer;
    @FXML
    private ImageView coverImg;
    @FXML
    private PlaybackController playbackController;
    private Playlist playlist;
    private Track[] tracks;

    @FXML
    private void initialize() {
        playlist = (Playlist) MainSceneController.getSelectedItem();
        var owner = ItemProvider.getItem(playlist.getOwnerId(), User.class);

        playlistNameLbl.setText(playlist.getName());
        ownerLlb.setText("By " + owner.getUsername());

        // TODO: creation date

        coverImg.setImage(ItemProvider.getImage(playlist.getImgPath()));
        tracks = GUIUtils.fillTrackContainerAndGenreLabel(playlist.getTracksIds(), trackContainer, genreLbl);

        saveBtn.setText(isSaved() ? "UNSAVE" : "SAVE");

        boolean ownedPlaylist = playlist.getOwnerId().equals(AccountManager.getUser().getId());
        saveBtn.setDisable(ownedPlaylist);
        manageBtn.setDisable(!ownedPlaylist);

        ManagePlaylistController.playlistUpdate.addListener(this::initialize);
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

    public void onSaveBtn(ActionEvent ignored) {
        if (isSaved()) {
            ItemProvider.saveItem(playlist.getId(), Playlist.class, false);
            saveBtn.setText("SAVE");
        } else {
            ItemProvider.saveItem(playlist.getId(), Playlist.class, true);
            saveBtn.setText("UNSAVE");
        }
        AccountManager.updateUser();
    }

    private boolean isSaved() {
        return Arrays.asList(AccountManager.getUser().getSavedPlaylistsIds()).contains(playlist.getId());
    }

    public void onManageBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(playlist);
        SceneManager.loadScene("/FXMLs/managePlaylist.fxml", this);
    }
}
