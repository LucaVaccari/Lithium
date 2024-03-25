package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.views.ManagePlaylistController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PlaylistEntry extends ItemEntry {
    @FXML
    private Node root;
    @FXML
    private ImageView coverImg;
    @FXML
    private Label nameLbl;
    @FXML
    private Label authorLbl;
    @FXML
    private Label nTracksLbl;

    public PlaylistEntry(Item playlist) {
        super(playlist);
        initialize();

        ManagePlaylistController.playlistUpdate.addListener(() -> {
            Item currentPlaylist = MainSceneController.getSelectedItem();
            if (currentPlaylist != null && currentPlaylist.equals(playlist)) {
                setItem(currentPlaylist);
                initialize();
            }
        });
    }

    private void initialize() {
        root.setOnMouseClicked(e -> {
            MainSceneController.setSelectedItem(item);
            SceneManager.loadScene("/FXMLs/itemViews/playlistView.fxml", this);
        });

        coverImg.setImage(ItemProvider.getImage(getPlaylist().getImgPath()));

        nameLbl.setText(getPlaylist().getName());

        var owner = ItemProvider.getItem(getPlaylist().getOwnerId(), User.class);
        authorLbl.setText("by " + owner.getUsername());

        int numberOfTracks = getPlaylist().getTracksIds().length;
        nTracksLbl.setText(numberOfTracks + (numberOfTracks == 1 ? " track" : " tracks"));
    }

    public Playlist getPlaylist() {
        return (Playlist) item;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/playlistEntry.fxml";
    }
}
