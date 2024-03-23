package it.unibs.pajc.lithium.gui.controllers;

import it.unibs.pajc.lithium.db.om.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainSceneController {
    @FXML
    private Tab playingNowTab;

    private static Item selectedItem;

    @FXML
    private void initialize() {
        playingNowTab.setDisable(true);
    }

    public static Item getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(Item selectedItem) {
        MainSceneController.selectedItem = selectedItem;
    }
}
