package it.unibs.pajc.lithium.gui.controllers;

import com.google.common.hash.Hashing;
import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.managers.AccountManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.nio.charset.StandardCharsets;

public class LoginController {
    public static final int MAX_USERNAME_LENGTH = 20;
    @FXML
    private HBox pswContainer;
    @FXML
    private HBox confirmPswContainer;
    @FXML
    private TextField usernameTxtField;
    @FXML
    private PasswordField pswTxtField;
    @FXML
    private PasswordField confirmPswTxtField;
    @FXML
    private Label messagesLabel;
    @FXML
    private Button submitBtn;

    private enum State {
        Start, Register, Login
    }

    private State state;
    private String validUsername;

    @FXML
    private void initialize() {
        backToStart();
    }

    public void onTxtFieldChanged(KeyEvent ignoredEvent) {
//        boolean disable =
//                usernameTxtField.getText().isEmpty() || pswTxtField.getText().isEmpty() && state != State.Start ||
//                        confirmPswTxtField.getText().isEmpty() && state == State.Register;
//        submitBtn.setDisable(disable);
        checkFieldsValidity();
    }

    private boolean checkFieldsValidity() {
        submitBtn.setDisable(false);
        String username = usernameTxtField.getText();
        String password = pswTxtField.getText();

        if (username.contains(" ")) {
            messagesLabel.setText("The username must not contain empty spaces");
            submitBtn.setDisable(true);
        }

        if (!username.matches("^[a-zA-Z0-9_]+")) {
            messagesLabel.setText("The username must contains only digits, letters or _");
            submitBtn.setDisable(true);
        }

        if (username.length() > MAX_USERNAME_LENGTH) {
            messagesLabel.setText("The username must be less than 20 characters");
            submitBtn.setDisable(true);
        }

        switch (state) {
            case Start -> validUsername = null;
            case Register -> {
                if (!username.equals(validUsername)) {
                    backToStart();
                    return false;
                }
                if (!password.equals(confirmPswTxtField.getText())) {
                    messagesLabel.setText("The passwords don't match");
                    submitBtn.setDisable(true);
                }

                if (password.contains(" ")) {
                    messagesLabel.setText("The password must not contain empty spaces");
                    submitBtn.setDisable(true);
                }

                if (password.isEmpty()) submitBtn.setDisable(true);
            }
            case Login -> {
                if (!username.equals(validUsername)) {
                    backToStart();
                    return false;
                }
                if (password.contains(" ")) {
                    messagesLabel.setText("The password must not contain empty spaces");
                    submitBtn.setDisable(true);
                }

                if (password.isEmpty()) submitBtn.setDisable(true);
            }
        }
        if (username.isEmpty()) submitBtn.setDisable(true);

        if (!submitBtn.isDisabled()) messagesLabel.setText("");
        return !submitBtn.isDisabled();
    }

    private void backToStart() {
        state = State.Start;
        pswContainer.setVisible(false);
        confirmPswContainer.setVisible(false);
        messagesLabel.setText("");
    }

    public void onSubmitBtn(ActionEvent ignoredEvent) {
        if (!checkFieldsValidity()) return;
        String username = usernameTxtField.getText();
        submitBtn.setDisable(true);
        if (state == State.Start) {
            var userExists = AccountManager.userExists(username);
            validUsername = username;
            pswContainer.setVisible(true);
            confirmPswContainer.setVisible(!userExists);
            submitBtn.setText(userExists ? "Login" : "Register");
            state = userExists ? State.Login : State.Register;
            pswTxtField.requestFocus();
            onTxtFieldChanged(null);
        } else {
            String password = pswTxtField.getText();
            String pswHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            if (state == State.Register) {
                AccountManager.registerUser(username, pswHash);
                state = State.Login;
                confirmPswContainer.setVisible(false);
                submitBtn.setText("Login");
                messagesLabel.setText("Successfully registered!");
            } else if (state == State.Login) {
                boolean auth = AccountManager.authenticateUser(username, pswHash);
                if (auth) {
                    messagesLabel.setText("Login successful");
                    AccountManager.saveLoginInfo(username, pswHash);
                    SceneManager.loadMainScene(this);
                } else {
                    messagesLabel.setText("Wrong password. Try again");
                    pswTxtField.setText("");
                }
            }
            onTxtFieldChanged(null);
        }
    }
}
