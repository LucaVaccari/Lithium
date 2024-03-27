package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.GUIUtils;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.PlaybackController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import it.unibs.pajc.lithium.managers.AccountManager;
import it.unibs.pajc.lithium.managers.PartyManager;
import it.unibs.pajc.lithium.managers.PlaybackManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.Objects;

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
    private Button playNowBtn;
    @FXML
    private Button playNextBtn;
    @FXML
    private Button addToQueueBtn;
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
        if (playlist == null) {
            SceneManager.backToPreviousScene();
            return;
        }
        update();

        ManagePlaylistController.playlistUpdate.addListener(playlist -> {
            if (playlist == null) {
                SceneManager.backToMainScene();
                return;
            }
            if (!Objects.equals(this.playlist.getId(), playlist.getId())) return;
            this.playlist = playlist;
            update();
        });

        PartyManager.partyJoined.addListener(
                partyId -> updateHost(PartyManager.anyPartyJoined() && !PartyManager.isHost()));
        PartyManager.hostUpdate.addListener(
                hostId -> updateHost(!Objects.equals(AccountManager.getUser().getId(), hostId)));
    }

    private void update() {
        var owner = ItemProvider.getItem(playlist.getOwnerId(), User.class);

        playlistNameLbl.setText(playlist.getName());
        ownerLlb.setText("By " + owner.getUsername());

        // TODO: creation date

        coverImg.setImage(ItemProvider.getImage(playlist.getImgPath()));
        tracks = GUIUtils.fillTrackContainerAndGenreLabel(playlist.getTrackIds(), trackContainer, genreLbl);

        saveBtn.setText(isSaved() ? "UNSAVE" : "SAVE");

        boolean ownedPlaylist = playlist.getOwnerId().equals(AccountManager.getUser().getId());
        saveBtn.setDisable(ownedPlaylist);
        manageBtn.setDisable(!ownedPlaylist);

        updateHost(PartyManager.anyPartyJoined() && !PartyManager.isHost());
    }

    private void updateHost(boolean inPartyAndNotHost) {
        playNowBtn.setDisable(inPartyAndNotHost);
        playNextBtn.setDisable(inPartyAndNotHost);
        addToQueueBtn.setDisable(inPartyAndNotHost);
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
        return Arrays.asList(AccountManager.getUser().getSavedPlaylistIds()).contains(playlist.getId());
    }

    public void onManageBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(playlist);
        SceneManager.loadScene("/FXMLs/managePlaylist.fxml", this);
    }
}
