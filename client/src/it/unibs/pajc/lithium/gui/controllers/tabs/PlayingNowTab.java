package it.unibs.pajc.lithium.gui.controllers.tabs;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.PlaybackManager;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class PlayingNowTab extends CustomComponent {
    @FXML
    private ImageView currentTrackImgCover;
    @FXML
    private Label currentTrackTitleLbl;
    @FXML
    private Label currentTrackArtistLbl;
    @FXML
    private Label currentTrackAlbumLbl;
    @FXML
    private ListView<TrackEntry> queueListView;

    @FXML
    private void initialize() {
        PlaybackManager.getUpdate().addListener(this::update);

        currentTrackTitleLbl.setOnMouseClicked(e -> {
            var track = PlaybackManager.getCurentTrack();
            if (track == null) return;
            MainSceneController.setSelectedItem(track);
            SceneManager.loadScene("/FXMLs/itemViews/trackView.fxml", this);
        });
        // TODO click on artist
        currentTrackAlbumLbl.setOnMouseClicked(e -> {
            var track = PlaybackManager.getCurentTrack();
            if (track == null) return;
            var album = ItemProvider.getItem(track.getAlbumId(), Album.class);
            MainSceneController.setSelectedItem(album);
            SceneManager.loadScene("/FXMLs/itemViews/albumView.fxml", this);
        });
    }

    public void update() {
        var track = PlaybackManager.getCurentTrack();
        if (track == null) return;
        var album = ItemProvider.getItem(track.getAlbumId(), Album.class);
        currentTrackImgCover.setImage(ItemProvider.getImage(album.getImgPath()));
        currentTrackTitleLbl.setText(track.getTitle());
        currentTrackArtistLbl.setText("By " + ItemProvider.getArtistNamesFormatted(track.getArtistsIds()));
        currentTrackAlbumLbl.setText(album.getTitle());

        // todo queue
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/playingNowTab.fxml";
    }
}
