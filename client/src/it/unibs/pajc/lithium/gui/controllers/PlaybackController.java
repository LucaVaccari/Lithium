package it.unibs.pajc.lithium.gui.controllers;

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
    }

    private void update() {
        // TODO: update visuals
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/playback.fxml";
    }
}
