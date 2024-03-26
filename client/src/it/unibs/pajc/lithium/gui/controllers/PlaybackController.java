package it.unibs.pajc.lithium.gui.controllers;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.managers.PlaybackManager;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class PlaybackController extends CustomComponent {
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
        progressSlider.valueChangingProperty().addListener((observable, oldVal, newVal) -> {
            sliderChanging = newVal;
        });
        progressSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
            if (sliderChanging) {
                PlaybackManager.seek(newVal.doubleValue());
            }
        });
        PlaybackManager.getUpdate().addListener(this::update);

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
        else currentlyPlayingLbl.setText(
                "%s - %s".formatted(ItemProvider.getArtistNamesFormatted(track.getArtistIds()), track.getTitle()));
        pauseBtn.setText(PlaybackManager.isPlaying() ? "Pause" : "Play");
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/playback.fxml";
    }
}
