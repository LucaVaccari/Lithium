package it.unibs.pajc.lithium.gui.controllers.tabs;

import it.unibs.pajc.lithium.managers.AccountManager;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SettingsTab extends CustomComponent {
    @FXML
    private Label loggedInAsLbl;
    @FXML
    private Button logOutBtn;

    @FXML
    private void initialize() {
        loggedInAsLbl.setText("Logged in as " + AccountManager.getUser().getUsername());
        logOutBtn.setOnAction(e -> {
            AccountManager.deleteLoginInfo();
            SceneManager.loadScene("/FXMLs/login.fxml", this);
        });
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/settingsTab.fxml";
    }
}
