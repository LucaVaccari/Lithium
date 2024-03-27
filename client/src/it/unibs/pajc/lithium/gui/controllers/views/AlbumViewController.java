package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.managers.AccountManager;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.managers.PlaybackManager;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.GUIUtils;
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
import java.util.Comparator;

public class AlbumViewController {
    // TODO: show track number
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
    private Button saveBtn;
    @FXML
    private ListView<TrackEntry> trackContainer;
    @FXML
    private PlaybackController playbackController;

    private Album album;
    private Track[] tracks;

    @FXML
    private void initialize() {
        album = (Album) MainSceneController.getSelectedItem();
        albumTitleLbl.setText(album.getTitle());
        artistLbl.setText(ItemProvider.getArtistNamesFormatted(album.getArtistsIds()));
        releaseDateLbl.setText(album.getReleaseDate());
        coverImg.setImage(ItemProvider.getImage(album.getImgPath()));
        tracks = GUIUtils.fillTrackContainerAndGenreLabel(album.getTrackIds(), trackContainer, genreLbl);
        trackContainer.getItems().sort(Comparator.comparingInt(t -> t.getTrack().getNumber()));
        saveBtn.setText(isSaved() ? "UNSAVE" : "SAVE");
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

    public void onSaveAlbumBtn(ActionEvent ignored) {
        if (isSaved()) {
            ItemProvider.saveItem(album.getId(), Album.class, false);
            saveBtn.setText("SAVE");
        } else {
            ItemProvider.saveItem(album.getId(), Album.class, true);
            saveBtn.setText("UNSAVE");
        }
        AccountManager.updateUser();
    }

    private boolean isSaved() {
        return Arrays.asList(AccountManager.getUser().getSavedAlbumIds()).contains(album.getId());
    }
}
