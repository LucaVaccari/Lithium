package it.unibs.pajc.lithium.gui.controllers;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.listEntries.PlaylistEntry;
import it.unibs.pajc.lithium.gui.controllers.views.ManagePlaylistController;
import it.unibs.pajc.lithium.managers.AccountManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TrackInPlaylistController {

    @FXML
    private ListView<HBox> playlistListView;

    private final Set<Playlist> selectedPlaylists = new HashSet<>();
    private Playlist[] playlists;
    private Track track;

    @FXML
    private void initialize() {
        track = (Track) MainSceneController.getSelectedItem();
        var playlistIds = AccountManager.getUser().getSavedPlaylistIds();
        playlists = ItemProvider.getItems(playlistIds, Playlist.class);

        Arrays.stream(playlists).filter(Objects::nonNull).forEach(playlist -> {
            var checkBox = new CheckBox();
            checkBox.setSelected(ItemProvider.trackInPlaylist(playlist, track.getId()));
            checkBox.selectedProperty().addListener(((observableValue, oldVal, newVal) -> {
                if (newVal) selectedPlaylists.add(playlist);
                else selectedPlaylists.remove(playlist);
            }));
            var hbox = new HBox(new PlaylistEntry(playlist, false), checkBox);
            hbox.setAlignment(Pos.CENTER);
            hbox.setFillHeight(true);
            playlistListView.getItems().add(hbox);
        });

        playlistListView.refresh();
    }

    public void onSave(ActionEvent e) {
        for (var p : playlists) {
            if (selectedPlaylists.contains(p)) ItemProvider.addTrackToPlaylist(p, track.getId());
            else ItemProvider.removeTrackFromPlaylist(p, track.getId());
            var newPlaylist = ItemProvider.getItem(p.getId(), Playlist.class);
            ManagePlaylistController.playlistUpdate.invoke(newPlaylist);
        }
        onCancel(e);
    }

    public void onCancel(ActionEvent ignored) {
        ((Stage) playlistListView.getScene().getWindow()).close();
        SceneManager.backToMainScene();
    }
}
