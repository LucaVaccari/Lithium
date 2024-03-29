package it.unibs.pajc.lithium.gui.controllers.tabs;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.GUIUtils;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.AlbumEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.ArtistEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.PlaylistEntry;
import it.unibs.pajc.lithium.gui.controllers.views.ManagePlaylistController;
import it.unibs.pajc.lithium.managers.AccountManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import kong.unirest.UnirestException;

import java.util.Arrays;
import java.util.Objects;

public class LibraryTab extends CustomComponent {
    @FXML
    private TextField searchTxtField;
    @FXML
    private ListView<AlbumEntry> albumContainer;
    @FXML
    private ListView<ArtistEntry> artistContainer;
    @FXML
    private ListView<PlaylistEntry> playlistContainer;
    @FXML
    private Button createPlaylistBtn;

    @FXML
    public void initialize() {
        searchTxtField.setOnKeyTyped(this::onSearchTxtFieldChange);
        onSearchTxtFieldChange(null);
        AccountManager.userUpdated.addListener(user -> Platform.runLater(() -> onSearchTxtFieldChange(null)));
        createPlaylistBtn.setOnAction(this::onCreatePlaylistBtn);
        ManagePlaylistController.playlistUpdate.addListener(playlist -> updatePlaylistList());
    }

    private void onSearchTxtFieldChange(KeyEvent event) {
        try {
            updateAlbumList();
            updateArtistList();
            updatePlaylistList();
        } catch (UnirestException e) {
            AlertUtil.showErrorAlert("HTTP error", "Error in SearchTab.java", e.getMessage());
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error in SearchTab.java", e.getMessage());
        }
    }

    private void updateAlbumList() throws UnirestException, JsonSyntaxException {
        albumContainer.getItems().clear();
        var albums = ItemProvider.getItems(AccountManager.getUser().getSavedAlbumIds(), Album.class);
        if (albums == null) {
            System.err.println("Null albums in search tab");
            return;
        }
        String searchTerm = searchTxtField.getText().toLowerCase();
        var filteredAlbums = Arrays.stream(albums).filter(a -> a.getTitle().toLowerCase().contains(searchTerm))
                .toArray(Album[]::new);
        GUIUtils.fillEntryList(albumContainer, filteredAlbums, AlbumEntry.class);
    }

    private void updateArtistList() throws UnirestException, JsonSyntaxException {
        artistContainer.getItems().clear();
        var artists = ItemProvider.getItems(AccountManager.getUser().getFollowedArtistIds(), Artist.class);
        if (artists == null) {
            System.err.println("Null items in search tab");
            return;
        }
        String searchTerm = searchTxtField.getText().toLowerCase();
        var filteredArtists = Arrays.stream(artists).filter(a -> a.getName().toLowerCase().contains(searchTerm))
                .toArray(Artist[]::new);
        GUIUtils.fillEntryList(artistContainer, filteredArtists, ArtistEntry.class);
    }

    private void updatePlaylistList() throws UnirestException, JsonSyntaxException {
        playlistContainer.getItems().clear();
        var playlists = ItemProvider.getItems(AccountManager.getUser().getSavedPlaylistIds(), Playlist.class);
        if (playlists == null) {
            System.err.println("Null items in search tab");
            return;
        }
        String searchTerm = searchTxtField.getText().toLowerCase();
        var filteredPlaylists = Arrays.stream(playlists).filter(Objects::nonNull)
                .filter(p -> p.getName().toLowerCase().contains(searchTerm)).toArray(Playlist[]::new);
        GUIUtils.fillEntryList(playlistContainer, filteredPlaylists, PlaylistEntry.class);
    }

    private void onCreatePlaylistBtn(ActionEvent ignored) {
        var playlistId = ItemProvider.createPlaylist();
        if (playlistId == -1) {
            AlertUtil.showErrorAlert("Error", "Server error", "Cannot create playlist");
            return;
        }
        Playlist newPlaylist = ItemProvider.getItem(playlistId, Playlist.class);
        MainSceneController.setSelectedItem(newPlaylist);
        SceneManager.loadScene("/FXMLs/managePlaylist.fxml", this);
        AccountManager.updateUser();
        ManagePlaylistController.playlistUpdate.invoke(newPlaylist);
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/libraryTab.fxml";
    }
}
