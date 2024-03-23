package it.unibs.pajc.lithium.gui.controllers.tabs;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.controllers.listEntries.*;
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

    private void onSearchTxtFieldChange(KeyEvent event) {
        String searchTerm = searchTxtField.getText().replace(" ", "+");
        try {
            var tracks = ItemProvider.searchItem(15, searchTerm, Track[].class, "track_title");
            var albums = ItemProvider.searchItem(15, searchTerm, Album[].class, "album_title");
            var artists = ItemProvider.searchItem(15, searchTerm, Artist[].class, "artist_name");
            var playlists = ItemProvider.searchItem(15, searchTerm, Playlist[].class, "playlist_title");

            if (tracks == null || albums == null || artists == null || playlists == null) {
                System.err.println("Null items in search tab");
                return;
            }

            EntryUtility.fillEntryList(trackContainer, tracks, TrackEntry.class);
            EntryUtility.fillEntryList(albumContainer, albums, AlbumEntry.class);
            EntryUtility.fillEntryList(artistContainer, artists, ArtistEntry.class);
            EntryUtility.fillEntryList(playlistContainer, playlists, PlaylistEntry.class);
        } catch (UnirestException e) {
            AlertUtil.showErrorAlert("HTTP error", "Error in SearchTab.java", e.getMessage());
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error in SearchTab.java", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        searchTxtField.setOnKeyTyped(this::onSearchTxtFieldChange);
        onSearchTxtFieldChange(null);
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/searchTab.fxml";
    }
}
