package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.gui.CustomComponent;

public class PlaylistEntry extends CustomComponent {
    private final Playlist playlist;

    public PlaylistEntry(Playlist playlist) {
        super();
        this.playlist = playlist;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/playlistEntry.fxml";
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
