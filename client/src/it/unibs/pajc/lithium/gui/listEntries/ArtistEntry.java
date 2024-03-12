package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.CustomComponent;

public class ArtistEntry extends CustomComponent {
    private final Artist artist;

    public ArtistEntry(Artist artist) {
        super();
        this.artist = artist;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLS/listComponents/artistEntry.fxml";
    }

    public Artist getArtist() {
        return artist;
    }
}
