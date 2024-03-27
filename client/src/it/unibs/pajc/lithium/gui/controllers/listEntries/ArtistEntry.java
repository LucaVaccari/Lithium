package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Artist;
import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ArtistEntry extends ItemEntry {
    // todo when follow update view
    @FXML
    private Node root;
    @FXML
    private ImageView proPicImg;
    @FXML
    private Label nameLbl;
    @FXML
    private Label followersLbl;

    public ArtistEntry(Item artist) {
        super(artist);
        initialize();
    }

    private void initialize() {
        root.setOnMouseClicked(e -> {
            MainSceneController.setSelectedItem(item);
            SceneManager.loadScene("/FXMLs/itemViews/artistView.fxml", this);
        });

        proPicImg.setImage(ItemProvider.getImage(getArtist().getProfilePicturePath()));

        nameLbl.setText(getArtist().getName());

        followersLbl.setText(getArtist().getFollowerIds().length + " followers");
    }

    public Artist getArtist() {
        return (Artist) item;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLS/listComponents/artistEntry.fxml";
    }
}
