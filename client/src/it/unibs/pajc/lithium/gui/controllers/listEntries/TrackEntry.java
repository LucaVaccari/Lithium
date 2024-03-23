package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class TrackEntry extends ItemEntry {
    @FXML
    private Node root;
    @FXML
    private ImageView coverImg;
    @FXML
    private Label titleLbl;
    @FXML
    private Label artistLbl;

    public TrackEntry(Item track) {
        super(track);
        initialize();
    }

    private void initialize() {
        root.setOnMouseClicked(e -> {
            MainSceneController.setSelectedItem(item);
            SceneManager.loadScene("/FXMLs/itemViews/trackView.fxml", this);
        });

        var album = ItemProvider.getItem(getTrack().getAlbumId(), Album.class);

        coverImg.setImage(ItemProvider.getImage(album.getImgPath()));

        titleLbl.setText(getTrack().getTitle());

        var artists = ItemProvider.getItems(album.getArtistsIds(), Artist.class);
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        artistLbl.setText(String.join(", ", artistNames));
    }

    public Track getTrack() {
        return (Track) item;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/trackEntry.fxml";
    }
}
