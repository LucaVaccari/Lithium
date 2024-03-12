package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.gui.CustomComponent;

public class AlbumEntry extends CustomComponent {
    private final Album album;

    public AlbumEntry(Album album) {
        super();
        this.album = album;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/albumEntry.fxml";
    }

    public Album getAlbum() {
        return album;
    }
}
