package it.unibs.pajc.lithium.gui.controllers;

import it.unibs.pajc.lithium.PlaybackManager;
import it.unibs.pajc.lithium.gui.CustomComponent;
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
    private void initialize() {
        update();

        backBtn.setOnAction(ignored -> PlaybackManager.previousTrack());
        pauseBtn.setOnAction(ignored -> PlaybackManager.togglePlay());
        forwardBtn.setOnAction(ignored -> PlaybackManager.nextTrack());
    }

    public void update() {
        progressSlider.setValue(PlaybackManager.getPlayPercentage());
        int currentTime = PlaybackManager.getCurrentTime();
        progressLbl.setText("%2d:%2d".formatted(currentTime / 60, currentTime % 60));
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/playback.fxml";
    }
}
