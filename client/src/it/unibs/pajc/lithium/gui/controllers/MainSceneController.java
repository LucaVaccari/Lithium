package it.unibs.pajc.lithium.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

public class MainSceneController {
    @FXML
    private Tab playingNowTab;
    @FXML
    private HBox playbackContainer;

    private static Object selectedItem;

    @FXML
    private void initialize() {
        playingNowTab.setDisable(true);
        playbackContainer.setDisable(true);
    }

    public static Object getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(Object selectedItem) {
        MainSceneController.selectedItem = selectedItem;
    }
}
