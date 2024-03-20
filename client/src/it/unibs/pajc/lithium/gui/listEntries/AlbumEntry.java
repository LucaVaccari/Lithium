package it.unibs.pajc.lithium.gui.listEntries;

import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class AlbumEntry extends CustomComponent {
    @FXML
    private ImageView coverImg;
    @FXML
    private Label titleLbl;
    @FXML
    private Label artistLbl;

    private final Album album;

    public AlbumEntry(Album album) {
        super();
        this.album = album;
        initialize();
    }

    private void initialize() {
        var imgBytes = HttpHandler.getBase64Img("/" + album.getImgPath());
        Image img = new Image(new ByteArrayInputStream(imgBytes));
        coverImg.setImage(img);

        titleLbl.setText(album.getTitle());

        var artists = new String[album.getArtistsIds().length];
        Integer[] artistsIds = album.getArtistsIds();
        for (int i = 0; i < artistsIds.length; i++) {
            var id = artistsIds[i];
            var json = HttpHandler.get("/artists/%d".formatted(id));
            var artist = ClientMain.getGson().fromJson(json, Artist.class);
            artists[i] = artist.getName();
        }
        artistLbl.setText(String.join(", ", artists));
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/albumEntry.fxml";
    }
}
