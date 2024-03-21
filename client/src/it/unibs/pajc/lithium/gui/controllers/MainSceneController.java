package it.unibs.pajc.lithium.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainSceneController {
    @FXML
    private Tab playingNowTab;

    private static Object selectedItem;

    @FXML
    private void initialize() {
        playingNowTab.setDisable(true);
    }

    public static Object getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(Object selectedItem) {
        MainSceneController.selectedItem = selectedItem;
    }
}
