package it.unibs.pajc.lithium.gui.controllers.tabs;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.GUIUtils;
import it.unibs.pajc.lithium.gui.controllers.listEntries.AlbumEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.ArtistEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.PlaylistEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import it.unibs.pajc.lithium.gui.controllers.views.ManagePlaylistController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import kong.unirest.UnirestException;

public class SearchTab extends CustomComponent {
    @FXML
    private TextField searchTxtField;
    @FXML
    private ListView<TrackEntry> trackContainer;
    @FXML
    private ListView<AlbumEntry> albumContainer;
    @FXML
    private ListView<ArtistEntry> artistContainer;
    @FXML
    private ListView<PlaylistEntry> playlistContainer;

    @FXML
    public void initialize() {
        searchTxtField.setOnKeyTyped(this::onSearchTxtFieldChange);
        onSearchTxtFieldChange(null);

        ManagePlaylistController.playlistUpdate.addListener(this::updatePlaylistList);
    }

    private void onSearchTxtFieldChange(KeyEvent event) {
        try {
            updateTrackList();
            updateAlbumList();
            updateArtistList();
            updatePlaylistList();
        } catch (UnirestException e) {
            AlertUtil.showErrorAlert("HTTP error", "Error in SearchTab.java", e.getMessage());
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error in SearchTab.java", e.getMessage());
        }
    }

    private void updateTrackList() throws UnirestException, JsonSyntaxException {
        var searchTerm = searchTxtField.getText().replace(" ", "+");
        var tracks = ItemProvider.searchItem(15, searchTerm, Track[].class, "track_title");
        if (tracks == null) {
            System.err.println("Null items in search tab");
            return;
        }
        GUIUtils.fillEntryList(trackContainer, tracks, TrackEntry.class);
    }

    private void updateAlbumList() throws UnirestException, JsonSyntaxException {
        var searchTerm = searchTxtField.getText().replace(" ", "+");
        var albums = ItemProvider.searchItem(15, searchTerm, Album[].class, "album_title");
        if (albums == null) {
            System.err.println("Null albums in search tab");
            return;
        }
        GUIUtils.fillEntryList(albumContainer, albums, AlbumEntry.class);
    }

    private void updateArtistList() throws UnirestException, JsonSyntaxException {
        var searchTerm = searchTxtField.getText().replace(" ", "+");
        var artists = ItemProvider.searchItem(15, searchTerm, Artist[].class, "artist_name");
        if (artists == null) {
            System.err.println("Null items in search tab");
            return;
        }
        GUIUtils.fillEntryList(artistContainer, artists, ArtistEntry.class);
    }

    private void updatePlaylistList() throws UnirestException, JsonSyntaxException {
        var searchTerm = searchTxtField.getText().replace(" ", "+");
        var playlists = ItemProvider.searchItem(15, searchTerm, Playlist[].class, "playlist_title");
        if (playlists == null) {
            System.err.println("Null items in search tab");
            return;
        }
        GUIUtils.fillEntryList(playlistContainer, playlists, PlaylistEntry.class);
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/searchTab.fxml";
    }
}
