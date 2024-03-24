package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Playlist;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import it.unibs.pajc.util.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.Arrays;

public class ManagePlaylistController {
    // TODO delete playlist btn
    @FXML
    private TextField nameTxtField;
    @FXML
    private Button nameSetBtn;
    @FXML
    private TextArea descriptionTxtArea;
    @FXML
    private Button descriptionSetBtn;
    @FXML
    private Label imgPathLbl;
    @FXML
    private ListView<HBox> trackView;
    @FXML
    private ImageView coverImg;
    @FXML
    private TextField searchTxtField;
    @FXML
    private ListView<HBox> searchTrackView;

    public final static Observer playlistUpdate = new Observer();
    private Playlist playlist;

    @FXML
    private void initialize() {
        playlist = (Playlist) MainSceneController.getSelectedItem();

        nameTxtField.setText(playlist.getName());
        descriptionTxtArea.setText(playlist.getDescription());
        imgPathLbl.setText("");
        coverImg.setImage(ItemProvider.getImage(playlist.getImgPath()));

        onNameSet(null);
        onDescriptionSet(null);
        onNameTyped(null);
        onDescriptionTyped(null);

        var tracks = ItemProvider.getItems(playlist.getTracksIds(), Track.class);
        trackView.getItems().clear();
        for (var track : tracks) {
            var hbox = getPlaylistTrackHbox(track);
            trackView.getItems().add(hbox);
        }

        onSearchTyped(null);
    }

    private static HBox getPlaylistTrackHbox(Track track) {
        var btn = new Button("Remove");
        // TODO: btn.setOnAction()
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.setMaxWidth(Double.MAX_VALUE);
        var trackEntry = new TrackEntry(track);
        trackEntry.setMaxHeight(Double.MAX_VALUE);
        trackEntry.setMaxWidth(Double.MAX_VALUE);
        var hbox = new HBox(trackEntry, btn);
        hbox.setAlignment(Pos.CENTER);
        hbox.setFillHeight(true);
        hbox.setMaxWidth(Double.MAX_VALUE);
        return hbox;
    }

    public void onNameSet(ActionEvent ignored) {
        var nameCandidate = nameTxtField.getText();
        if (nameCandidate.isEmpty() || nameCandidate.equals(playlist.getName())) return;
        playlist.setName(nameCandidate);
        ItemProvider.updateItem(playlist.getId(), Playlist.class, "playlist_title=" + nameCandidate);
        update();
        onNameTyped(null);
    }

    public void onDescriptionSet(ActionEvent ignored) {
        var descriptionCandidate = descriptionTxtArea.getText();
        if (descriptionCandidate.isEmpty() || descriptionCandidate.equals(playlist.getDescription())) return;
        playlist.setDescription(descriptionCandidate);
        ItemProvider.updateItem(playlist.getId(), Playlist.class, "playlist_description=" + descriptionCandidate);
        update();
        onDescriptionTyped(null);
    }

    public void onSelectImg(ActionEvent ignored) {
        // TODO image select and upload
    }

    public void onDescriptionTyped(KeyEvent ignored) {
        descriptionSetBtn.setDisable(playlist.getDescription().equals(descriptionTxtArea.getText()));
    }

    public void onNameTyped(KeyEvent ignored) {
        nameSetBtn.setDisable(playlist.getName().equals(nameTxtField.getText()));
    }

    public void onSearchTyped(KeyEvent ignored) {
        var searchTerm = searchTxtField.getText();
        var tracks = ItemProvider.searchItem(15, searchTerm, Track[].class, "track_title");
        if (tracks == null) {
            System.err.println("Null items in search tab");
            return;
        }
        for (var track : tracks) {
            if (searchTrackView.getItems().filtered(a -> ((TrackEntry) a.getChildren().getFirst()).getTrack().equals(track))
                    .isEmpty()) {
                var btn = new Button("Add");
                // TODO: btn.setOnAction()
                btn.setMaxHeight(Double.MAX_VALUE);
                btn.setMaxWidth(Double.MAX_VALUE);
                var trackEntry = new TrackEntry(track);
                trackEntry.setMaxHeight(Double.MAX_VALUE);
                trackEntry.setMaxWidth(Double.MAX_VALUE);
                var hbox = new HBox(trackEntry, btn);
                hbox.setAlignment(Pos.CENTER);
                hbox.setFillHeight(true);
                hbox.setMaxWidth(Double.MAX_VALUE);
                searchTrackView.getItems().add(hbox);
            }
        }
        searchTrackView.getItems().removeIf(
                hbox -> !Arrays.stream(tracks).toList().contains(((TrackEntry) hbox.getChildren().getFirst()).getTrack()));
        searchTrackView.refresh();
    }

    private void update() {
        MainSceneController.setSelectedItem(ItemProvider.getItem(playlist.getId(), Playlist.class, true));
        playlistUpdate.invoke();
    }
}
