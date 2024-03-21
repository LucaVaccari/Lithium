package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.Objects;

public class TrackEntry extends CustomComponent {
    @FXML
    private Node root;
    @FXML
    private ImageView coverImg;
    @FXML
    private Label titleLbl;
    @FXML
    private Label artistLbl;

    private final Track track;

    public TrackEntry(Track track) {
        super();
        this.track = track;
        initialize();
        root.setOnMouseClicked(e -> {
            MainSceneController.setSelectedItem(track);
            SceneManager.loadScene("/FXMLs/itemViews/trackView.fxml", this);
        });
    }

    private void initialize() {
//        var albumJson = HttpHandler.get("/album/" + track.getAlbumId());
//        var album = ItemProvider.getGson().fromJson(albumJson, Album.class);
        var album = ItemProvider.getItem(track.getAlbumId(), Album.class);

        coverImg.setImage(ItemProvider.getImage("/" + album.getImgPath()));

        titleLbl.setText(track.getTitle());

        var artists = ItemProvider.getItems(album.getArtistsIds(), Artist.class);
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        artistLbl.setText(String.join(", ", artistNames));
    }

    public Track getTrack() {
        return track;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/trackEntry.fxml";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackEntry that = (TrackEntry) o;
        return Objects.equals(track, that.track);
    }

    @Override
    public int hashCode() {
        return Objects.hash(track);
    }
}
