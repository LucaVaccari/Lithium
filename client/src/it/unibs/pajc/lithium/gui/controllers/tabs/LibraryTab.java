package it.unibs.pajc.lithium.gui.controllers.tabs;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.controllers.listEntries.AlbumEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.ArtistEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.EntryUtility;
import it.unibs.pajc.lithium.gui.controllers.listEntries.PlaylistEntry;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import kong.unirest.UnirestException;

public class LibraryTab extends CustomComponent {
    @FXML
    private TextField searchTxtField;
    @FXML
    private ListView<AlbumEntry> albumContainer;
    @FXML
    private ListView<ArtistEntry> artistContainer;
    @FXML
    private ListView<PlaylistEntry> playlistContainer;

    private void onSearchTxtFieldChange(KeyEvent event) {
        try {
            var albums = ItemProvider.getItems(ClientMain.getUser().getSavedAlbumsIds(), Album.class);
            var artists = ItemProvider.getItems(ClientMain.getUser().getFollowedArtistsIds(), Artist.class);
            var playlists = ItemProvider.getItems(ClientMain.getUser().getSavedPlaylistsIds(), Playlist.class);

            if (albums == null || artists == null || playlists == null) {
                System.err.println("Null items in search tab");
                return;
            }

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
        return "/FXMLs/mainTabs/libraryTab.fxml";
    }
}
