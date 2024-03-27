package it.unibs.pajc.lithium.gui.controllers;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.managers.AccountManager;
import it.unibs.pajc.lithium.managers.PartyManager;
import it.unibs.pajc.lithium.managers.PlaybackManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.Objects;

public class PlaybackController extends CustomComponent {
    // todo disable buttons when in party
    @FXML
    private Button backBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button forwardBtn;
    @FXML
    private Slider progressSlider;
    @FXML
    private Label progressLbl;
    @FXML
    private Label currentlyPlayingLbl;

    private AnimationTimer timer;
    private boolean sliderChanging;

    @FXML
    private void initialize() {
        update(PlaybackManager.getCurentTrack());
        backBtn.setOnAction(ignored -> PlaybackManager.previousTrack());
        pauseBtn.setOnAction(ignored -> PlaybackManager.togglePlay());
        forwardBtn.setOnAction(ignored -> PlaybackManager.nextTrack());
        progressSlider.setBlockIncrement(0.1);
        progressSlider.valueChangingProperty().addListener((observable, oldVal, newVal) -> sliderChanging = newVal);
        progressSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
            if (sliderChanging) {
                PlaybackManager.seek(newVal.doubleValue());
            }
        });
        PlaybackManager.getUpdate().addListener(this::update);
        PartyManager.partyJoined.addListener(
                partyId -> updateHost(PartyManager.anyPartyJoined() && !PartyManager.isHost()));
        PartyManager.hostUpdate.addListener(
                hostId -> updateHost(!Objects.equals(AccountManager.getUser().getId(), hostId)));

        if (timer != null) return;
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                Track track = PlaybackManager.getCurentTrack();
                if (track == null) return;
                if (!sliderChanging) progressSlider.setValue(PlaybackManager.getCurrentTime());
                int currentTime = PlaybackManager.getCurrentTime();
                progressLbl.setText("%02d:%02d".formatted(currentTime / 60, currentTime % 60));
            }
        };
        timer.start();
    }

    private void update(Track track) {
        progressSlider.setMin(0);
        progressSlider.setMax(PlaybackManager.getMaxTime());
        if (track == null) currentlyPlayingLbl.setText("Nothing is playing");
        else currentlyPlayingLbl.setText(ItemProvider.getArtistTrackFormatted(track));
        pauseBtn.setText(PlaybackManager.isPlaying() ? "Pause" : "Play");
        updateHost(PartyManager.anyPartyJoined() && !PartyManager.isHost());
    }

    private void updateHost(boolean inPartyAndNotHost) {
        backBtn.setDisable(inPartyAndNotHost);
        pauseBtn.setDisable(inPartyAndNotHost);
        forwardBtn.setDisable(inPartyAndNotHost);
        progressSlider.setDisable(inPartyAndNotHost);
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/playback.fxml";
    }
}
