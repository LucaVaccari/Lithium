package it.unibs.pajc.lithium.gui.controllers;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.listEntries.AlbumEntry;
import it.unibs.pajc.lithium.gui.listEntries.ArtistEntry;
import it.unibs.pajc.lithium.gui.listEntries.PlaylistEntry;
import it.unibs.pajc.lithium.gui.listEntries.TrackEntry;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import kong.unirest.UnirestException;


public class SearchTab extends CustomComponent {
    @FXML
    private ListView<TrackEntry> trackContainer;
    @FXML
    private ListView<AlbumEntry> albumContainer;
    @FXML
    private ListView<ArtistEntry> artistContainer;
    @FXML
    private ListView<PlaylistEntry> playlistContainer;

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/searchTab.fxml";
    }

    @FXML
    private void initialize() {
        // TODO: search tab functionality

        try {
            String tracksJson = HttpHandler.get("/track?number-of-results=10");
            String albumsJson = HttpHandler.get("/album?number-of-results=10");
            String artistsJson = HttpHandler.get("/artist?number-of-results=10");
            String playlistsJson = HttpHandler.get("/playlist?number-of-results=10");
            var tracks = ClientMain.getGson().fromJson(tracksJson, Track[].class);
            var albums = ClientMain.getGson().fromJson(albumsJson, Album[].class);
            var artists = ClientMain.getGson().fromJson(artistsJson, Artist[].class);
            var playlists = ClientMain.getGson().fromJson(playlistsJson, Playlist[].class);

            for (var track : tracks) trackContainer.getItems().add(new TrackEntry(track));
            for (var album : albums) albumContainer.getItems().add(new AlbumEntry(album));
            for (var artist : artists) artistContainer.getItems().add(new ArtistEntry(artist));
            for (var playlist : playlists) playlistContainer.getItems().add(new PlaylistEntry(playlist));
        } catch (UnirestException e) {
            AlertUtil.showErrorAlert("HTTP error", "Error in SearchTab.java", e.getMessage());
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error in SearchTab.java", e.getMessage());
            e.printStackTrace();
        }
    }
}
