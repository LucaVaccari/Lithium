package it.unibs.pajc.lithium.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class LoginController {
    @FXML
    private TextField usernameTxtField;
    @FXML
    private PasswordField passwordTxtField;
    @FXML
    private PasswordField confirmPasswordTxtField;
    @FXML
    private Button submitBtn;

    public void onUsernameTxtFieldChanged(KeyEvent ignoredEvent) {
        submitBtn.setDisable(usernameTxtField.getText().isEmpty());
    }

    public void onSubmitBtn(ActionEvent event) {
        // TODO: show psw prompt, login or register based on other inputs
    }
}
