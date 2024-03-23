package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class AlbumEntry extends ItemEntry {
    @FXML
    private Node root;
    @FXML
    private ImageView coverImg;
    @FXML
    private Label titleLbl;
    @FXML
    private Label artistLbl;

    public AlbumEntry(Item album) {
        super(album);
        initialize();
    }

    protected void initialize() {
        root.setOnMouseClicked(e -> {
            MainSceneController.setSelectedItem(item);
            SceneManager.loadScene("/FXMLs/itemViews/albumView.fxml", this);
        });

        coverImg.setImage(ItemProvider.getImage(getAlbum().getImgPath()));

        titleLbl.setText(getAlbum().getTitle());

        var artists = ItemProvider.getItems(getAlbum().getArtistsIds(), Artist.class);
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        artistLbl.setText(String.join(", ", artistNames));
    }

    public Album getAlbum() {
        return (Album) item;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/albumEntry.fxml";
    }
}
