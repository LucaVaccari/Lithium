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

    private final boolean clickable;

    public PlaylistEntry(Item playlist, boolean clickable) {
        super(playlist);
        this.clickable = clickable;
        initialize();
    }

    public PlaylistEntry(Item playlist) {
        super(playlist);
        clickable = true;
        initialize();
    }

    private void initialize() {
        if (clickable) {
            root.setOnMouseClicked(e -> {
                MainSceneController.setSelectedItem(item);
                SceneManager.loadScene("/FXMLs/itemViews/playlistView.fxml", this);
            });
        }

        coverImg.setImage(ItemProvider.getImage(getPlaylist().getImgPath()));

        nameLbl.setText(getPlaylist().getName());

        var owner = ItemProvider.getItem(getPlaylist().getOwnerId(), User.class);
        authorLbl.setText("by " + owner.getUsername());

        int numberOfTracks = getPlaylist().getTrackIds().length;
        nTracksLbl.setText(numberOfTracks + (numberOfTracks == 1 ? " track" : " tracks"));

        ManagePlaylistController.playlistUpdate.addListener(playlist -> {
            if (playlist == null || !playlist.equals(getPlaylist())) return;
            setItem(playlist);
            initialize();
        });
    }

    public Playlist getPlaylist() {
        return (Playlist) item;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/playlistEntry.fxml";
    }
}
