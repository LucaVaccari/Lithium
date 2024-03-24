package it.unibs.pajc.lithium.gui.controllers.views;

import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.gui.controllers.MainSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ViewNavbar extends CustomComponent {
    @FXML
    private Button backBtn;
    @FXML
    private Button homeBtn;

    @FXML
    private void initialize() {
        backBtn.setOnAction(ViewNavbar::onBackBtn);
        homeBtn.setOnAction(ViewNavbar::onHomeBtn);
    }

    private static void onBackBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToPreviousScene();
    }

    private static void onHomeBtn(ActionEvent ignored) {
        MainSceneController.setSelectedItem(null);
        SceneManager.backToMainScene();
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/itemViews/viewNavbar.fxml";
    }
}
