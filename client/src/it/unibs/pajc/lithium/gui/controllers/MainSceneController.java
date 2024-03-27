package it.unibs.pajc.lithium.gui.controllers;

import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.managers.PlaybackManager;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainSceneController {
    @FXML
    private Tab playingNowTab;
    @FXML
    private Tab partiesTab;

    private static Item selectedItem;

    @FXML
    private void initialize() {
        disableTabs(true);
        PlaybackManager.getUpdate().addListener(track -> {
            disableTabs(track == null);
        });
    }

    private void disableTabs(boolean disable) {
        playingNowTab.setDisable(disable);
        partiesTab.setDisable(disable);
    }

    public static Item getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(Item selectedItem) {
        MainSceneController.selectedItem = selectedItem;
    }
}
