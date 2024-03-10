package it.unibs.pajc.lithium.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

public class MainSceneController {
    @FXML
    private Tab playingNowTab;
    @FXML
    private HBox playbackContainer;

    @FXML
    private void initialize() {
        playingNowTab.setDisable(true);
        playbackContainer.setDisable(true);
    }
}
