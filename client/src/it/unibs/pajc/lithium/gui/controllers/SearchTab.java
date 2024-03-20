package it.unibs.pajc.lithium.gui.controllers;

import com.google.gson.JsonSyntaxException;
import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.listEntries.AlbumEntry;
import it.unibs.pajc.lithium.gui.listEntries.ArtistEntry;
import it.unibs.pajc.lithium.gui.listEntries.PlaylistEntry;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import kong.unirest.UnirestException;

public class SearchTab extends CustomComponent {
    @FXML
    private VBox trackContainer;
    @FXML
    private VBox albumContainer;
    @FXML
    private VBox artistContainer;
    @FXML
    private VBox playlistContainer;

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/searchTab.fxml";
    }

    @FXML
    private void initialize() {
        // TODO: search tab functionality

        try {
            String albumsJson = HttpHandler.get("/albums?number-of-results=10");
            String artistsJson = HttpHandler.get("/artists?number-of-results=10");
            String playlistsJson = HttpHandler.get("/playlists?number-of-results=10");
            var albums = ClientMain.getGson().fromJson(albumsJson, Album[].class);
            var artists = ClientMain.getGson().fromJson(artistsJson, Artist[].class);
            var playlists = ClientMain.getGson().fromJson(playlistsJson, Playlist[].class);

            for (var album : albums) albumContainer.getChildren().add(new AlbumEntry(album));
            for (var artist : artists) artistContainer.getChildren().add(new ArtistEntry(artist));
            for (var playlist : playlists) playlistContainer.getChildren().add(new PlaylistEntry(playlist));
        } catch (UnirestException e) {
            AlertUtil.showErrorAlert("HTTP error", "Error in SearchTab.java", e.getMessage());
        } catch (JsonSyntaxException e) {
            AlertUtil.showErrorAlert("JSON error", "Error in SearchTab.java", e.getMessage());
            e.printStackTrace();
        }
    }
}
