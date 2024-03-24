package it.unibs.pajc.lithium.gui.controllers.tabs;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.AccountManager;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.GUIUtils;
import it.unibs.pajc.lithium.gui.controllers.listEntries.AlbumEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.ArtistEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.PlaylistEntry;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import kong.unirest.UnirestException;

import java.util.Arrays;

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
        AccountManager.userUpdated.addListener(() -> Platform.runLater(() -> onSearchTxtFieldChange(null)));
        createPlaylistBtn.setOnAction(this::onCreatePlaylistBtn);
    }

    private void onSearchTxtFieldChange(KeyEvent event) {
        try {
            var albums = ItemProvider.getItems(AccountManager.getUser().getSavedAlbumsIds(), Album.class);
            var artists = ItemProvider.getItems(AccountManager.getUser().getFollowedArtistsIds(), Artist.class);
            var playlists = ItemProvider.getItems(AccountManager.getUser().getSavedPlaylistsIds(), Playlist.class);


            if (albums == null || artists == null || playlists == null) {
                System.err.println("Null items in search tab");
                return;
            }

            String searchTerm = searchTxtField.getText().toLowerCase();
            var filteredAlbums = Arrays.stream(albums).filter(a -> a.getTitle().toLowerCase().contains(searchTerm))
                    .toArray(Album[]::new);
            var filteredArtists = Arrays.stream(artists).filter(a -> a.getName().toLowerCase().contains(searchTerm))
                    .toArray(Artist[]::new);
            var filteredPlaylists = Arrays.stream(playlists).filter(p -> p.getName().toLowerCase().contains(searchTerm))
                    .toArray(Playlist[]::new);
            GUIUtils.fillEntryList(albumContainer, filteredAlbums, AlbumEntry.class);
            GUIUtils.fillEntryList(artistContainer, filteredArtists, ArtistEntry.class);
            GUIUtils.fillEntryList(playlistContainer, filteredPlaylists, PlaylistEntry.class);
        } catch (UnirestException e) {
            AlertUtil.showErrorAlert("HTTP error", "Error in SearchTab.java", e.getMessage());
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error in SearchTab.java", e.getMessage());
        }
    }

    private void onCreatePlaylistBtn(ActionEvent ignored) {
        // TODO create playlist button
        System.out.println("Create playlist...");
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/libraryTab.fxml";
    }
}
