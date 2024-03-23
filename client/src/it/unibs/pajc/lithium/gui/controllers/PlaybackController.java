package it.unibs.pajc.lithium.gui.controllers;

import it.unibs.pajc.lithium.PlaybackManager;
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

    private static AnimationTimer timer;

    @FXML
    private void initialize() {
        backBtn.setOnAction(ignored -> PlaybackManager.previousTrack());
        pauseBtn.setOnAction(ignored -> PlaybackManager.togglePlay());
        forwardBtn.setOnAction(ignored -> PlaybackManager.nextTrack());
        progressSlider.setMin(0);

        if (timer != null) return;
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                progressSlider.setMax(PlaybackManager.getMaxTime());
                progressSlider.setValue(PlaybackManager.getCurrentTime());
                int currentTime = PlaybackManager.getCurrentTime();
                progressLbl.setText("%02d:%02d".formatted(currentTime / 60, currentTime % 60));
                Track track = PlaybackManager.getCurentTrack();
                currentlyPlayingLbl.setText("%s".formatted(track != null ? track.getTitle() : "Nothing is playing"));
            }
        };
        timer.start();
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/playback.fxml";
    }
}
