package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.Objects;

public class AlbumEntry extends CustomComponent {
    @FXML
    private Node root;
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
        root.setOnMouseClicked(e -> {
            MainSceneController.setSelectedItem(album);
            SceneManager.loadScene("/FXMLs/itemViews/albumView.fxml", this, false);
        });
    }

    private void initialize() {
        coverImg.setImage(ItemProvider.getImage("/" + album.getImgPath()));

        titleLbl.setText(album.getTitle());

        var artists = ItemProvider.getItems(album.getArtistsIds(), Artist.class);
        var artistNames = Arrays.stream(artists).map(Artist::getName).toArray(String[]::new);
        artistLbl.setText(String.join(", ", artistNames));
    }

    public Album getAlbum() {
        return album;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/albumEntry.fxml";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumEntry that = (AlbumEntry) o;
        return Objects.equals(album, that.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(album);
    }
}
