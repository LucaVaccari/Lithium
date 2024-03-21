package it.unibs.pajc.lithium.gui.controllers;

import com.google.common.hash.Hashing;
import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.gui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LoginController {
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

    @FXML
    private void initialize() {
        state = State.Start;
        pswContainer.setVisible(false);
        confirmPswContainer.setVisible(false);
        messagesLabel.setText("");
    }

    public void onTxtFieldChanged(KeyEvent ignoredEvent) {
        boolean disable =
                usernameTxtField.getText().isEmpty() || pswTxtField.getText().isEmpty() && state != State.Start ||
                        confirmPswTxtField.getText().isEmpty() && state == State.Register;
        submitBtn.setDisable(disable);

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

        switch (state) {
            case Start -> {
            }
            case Register -> {
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
                if (password.contains(" ")) {
                    messagesLabel.setText("The password must not contain empty spaces");
                    submitBtn.setDisable(true);
                }

                if (password.isEmpty()) submitBtn.setDisable(true);
            }
        }
        if (username.isEmpty()) submitBtn.setDisable(true);

        return !submitBtn.isDisabled();
    }

    public void onSubmitBtn(ActionEvent ignoredEvent) {
        if (!checkFieldsValidity()) return;
        String username = usernameTxtField.getText();
        if (state == State.Start) {
            boolean userExists = Boolean.parseBoolean(HttpHandler.get("/user/exists", new HashMap<>() {{
                put("username", username);
            }}));

            pswContainer.setVisible(true);
            confirmPswContainer.setVisible(!userExists);
            submitBtn.setText(userExists ? "Login" : "Register");
            state = userExists ? State.Login : State.Register;
            onTxtFieldChanged(null);
        } else {
            String password = pswTxtField.getText();
            String pswHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            if (state == State.Register) {
                HttpHandler.post("/user/register", username + "," + pswHash);
                state = State.Login;
                confirmPswContainer.setVisible(false);
                submitBtn.setText("Login");
                messagesLabel.setText("Successfully registered!");
            } else if (state == State.Login) {
                boolean auth = Boolean.parseBoolean(HttpHandler.post("/user/auth", username + "," + pswHash));
                if (auth) {
                    messagesLabel.setText("Login successful");
                    SceneManager.loadScene("/FXMLs/mainScene.fxml", this);
                } else {
                    messagesLabel.setText("Wrong password. Try again");
                    pswTxtField.setText("");
                }
            }
            onTxtFieldChanged(null);
        }
    }
}
