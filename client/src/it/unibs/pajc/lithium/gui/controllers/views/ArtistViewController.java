package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Track;
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
    @FXML
    private Button playNowBtn;
    @FXML
    private Button playNextBtn;
    @FXML
    private Button addToQueueBtn;
    @FXML
    private Button followBtn;
    @FXML
    private PlaybackController playbackController;
    private Artist artist;
    private Track[] tracks;

    @FXML
    private void initialize() {
        artist = (Artist) MainSceneController.getSelectedItem();
        artistNameLbl.setText(artist.getName());
        bioLbl.setText(artist.getBio());

        proPicImg.setImage(ItemProvider.getImage(artist.getProfilePicturePath()));

        // TODO: track container (get tracks from artists)
        // TODO genres

        followBtn.setText(isFollowed() ? "UNFOLLOW" : "FOLLOW");

        updateHost(PartyManager.joinedAndNotHost());
        PartyManager.partyJoined.addListener(partyId -> updateHost(PartyManager.joinedAndNotHost()));
        PartyManager.hostUpdate.addListener(
                hostId -> updateHost(!Objects.equals(AccountManager.getUser().getId(), hostId)));
    }

    private void updateHost(boolean inPartyAndNotHost) {
        /*playNowBtn.setDisable(inPartyAndNotHost);
        playNextBtn.setDisable(inPartyAndNotHost);
        addToQueueBtn.setDisable(inPartyAndNotHost);*/
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

    public void onFollowBtn(ActionEvent ignored) {
        if (isFollowed()) {
            ItemProvider.saveItem(artist.getId(), Artist.class, false);
            followBtn.setText("FOLLOW");
        } else {
            ItemProvider.saveItem(artist.getId(), Artist.class, true);
            followBtn.setText("UNFOLLOW");
        }
        AccountManager.updateUser();
    }

    private boolean isFollowed() {
        return Arrays.asList(AccountManager.getUser().getFollowedArtistIds()).contains(artist.getId());
    }
}
