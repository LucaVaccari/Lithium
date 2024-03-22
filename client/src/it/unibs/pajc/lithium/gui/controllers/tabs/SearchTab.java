package it.unibs.pajc.lithium.gui.controllers.tabs;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.controllers.listEntries.AlbumEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.ArtistEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.PlaylistEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import kong.unirest.UnirestException;

import java.util.Arrays;

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

            for (var track : tracks) {
                if (trackContainer.getItems().filtered(t -> t.getTrack().equals(track)).isEmpty())
                    trackContainer.getItems().add(new TrackEntry(track));
            }
            for (var album : albums) {
                if (albumContainer.getItems().filtered(a -> a.getAlbum().equals(album)).isEmpty())
                    albumContainer.getItems().add(new AlbumEntry(album));
            }
            for (var artist : artists) {
                if (artistContainer.getItems().filtered(a -> a.getArtist().equals(artist)).isEmpty())
                    artistContainer.getItems().add(new ArtistEntry(artist));
            }
            for (var playlist : playlists) {
                if (playlistContainer.getItems().filtered(p -> p.getPlaylist().equals(playlist)).isEmpty())
                    playlistContainer.getItems().add(new PlaylistEntry(playlist));
            }
            trackContainer.getItems()
                    .removeIf(trackEntry -> !Arrays.stream(tracks).toList().contains(trackEntry.getTrack()));
            albumContainer.getItems()
                    .removeIf(albumEntry -> !Arrays.stream(albums).toList().contains(albumEntry.getAlbum()));
            artistContainer.getItems()
                    .removeIf(artistEntry -> !Arrays.stream(artists).toList().contains(artistEntry.getArtist()));
            playlistContainer.getItems().removeIf(
                    playlistEntry -> !Arrays.stream(playlists).toList().contains(playlistEntry.getPlaylist()));

        } catch (UnirestException e) {
            AlertUtil.showErrorAlert("HTTP error", "Error in SearchTab.java", e.getMessage());
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error in SearchTab.java", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        searchTxtField.setOnKeyTyped(this::onSearchTxtFieldChange);
        onSearchTxtFieldChange(null);
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/searchTab.fxml";
    }
}
