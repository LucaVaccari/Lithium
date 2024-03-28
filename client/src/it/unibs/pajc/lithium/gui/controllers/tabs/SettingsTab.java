package it.unibs.pajc.lithium.gui.controllers.tabs;

import it.unibs.pajc.lithium.Config;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.managers.AccountManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SettingsTab extends CustomComponent {
    @FXML
    private TextField httpHostTxtField;
    @FXML
    private TextField httpPortTxtField;
    @FXML
    private TextField lcpHostTxtField;
    @FXML
    private TextField lcpPortTxtField;
    @FXML
    private Label loggedInAsLbl;
    @FXML
    private Button logOutBtn;

    @FXML
    private void initialize() {
        // force the field to be numeric only
        httpPortTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                httpPortTxtField.setText(newValue.replaceAll("\\D", ""));
            }
        });
        lcpPortTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                lcpPortTxtField.setText(newValue.replaceAll("\\D", ""));
            }
        });

        httpHostTxtField.setText(Config.getServerHttpUrl());
        httpPortTxtField.setText(String.valueOf(Config.getServerHttpPort()));
        lcpHostTxtField.setText(Config.getServerLcpUrl());
        lcpPortTxtField.setText(String.valueOf(Config.getServerLcpPort()));

        httpHostTxtField.setOnAction(e -> Config.setServerHttpUrl(httpHostTxtField.getText()));
        httpPortTxtField.setOnAction(e -> Config.setServerHttpPort(Integer.parseInt(httpPortTxtField.getText())));
        lcpHostTxtField.setOnAction(e -> Config.setServerLcpUrl(httpHostTxtField.getText()));
        lcpPortTxtField.setOnAction(e -> Config.setServerLcpPort(Integer.parseInt(lcpPortTxtField.getText())));

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
