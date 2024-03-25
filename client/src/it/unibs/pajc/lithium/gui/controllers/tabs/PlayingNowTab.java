package it.unibs.pajc.lithium.gui.controllers.tabs;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.PlaybackManager;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;

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
    private ListView<HBox> queueListView;

    private int currentSelectedTrackIndex;

    @FXML
    private void initialize() {
        PlaybackManager.getUpdate().addListener(this::update);
        update(PlaybackManager.getCurentTrack());

        queueListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal.intValue() != currentSelectedTrackIndex)
                queueListView.getSelectionModel().select(currentSelectedTrackIndex);
        });

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

    public void update(Track currentTrack) {
        if (currentTrack == null) {
            currentTrackImgCover.setImage(null);
            currentTrackTitleLbl.setText("");
            currentTrackArtistLbl.setText("");
            currentTrackAlbumLbl.setText("");
            return;
        }

        var album = ItemProvider.getItem(currentTrack.getAlbumId(), Album.class);
        currentTrackImgCover.setImage(ItemProvider.getImage(album.getImgPath()));
        currentTrackTitleLbl.setText(currentTrack.getTitle());
        currentTrackArtistLbl.setText("By " + ItemProvider.getArtistNamesFormatted(currentTrack.getArtistIds()));
        currentTrackAlbumLbl.setText(album.getTitle());

        queueListView.getItems().clear();
        List<Track> trackQueue = PlaybackManager.getTrackQueue();
        boolean currentReached = false;
        for (int i = 0; i < trackQueue.size(); i++) {
            Track track = trackQueue.get(i);
            var entry = new TrackEntry(track);
            var btn = new Button("Remove");
            btn.setOnAction(e -> PlaybackManager.removeFromQueue(track));
            btn.setDisable(!currentReached);
            var hbox = new HBox(entry, btn);
            hbox.setAlignment(Pos.CENTER);
            queueListView.getItems().add(hbox);
            if (track.equals(currentTrack)) {
                queueListView.getSelectionModel().select(i);
                queueListView.scrollTo(i);
                currentReached = true;
                currentSelectedTrackIndex = i;
            }
        }
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/playingNowTab.fxml";
    }
}
