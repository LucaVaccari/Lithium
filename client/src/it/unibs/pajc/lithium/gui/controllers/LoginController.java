package it.unibs.pajc.lithium.gui.controllers;

import com.google.common.hash.Hashing;
import it.unibs.pajc.lithium.HttpHandler;
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

        if (state == State.Register) {
            boolean pswMatch = pswTxtField.getText().equals(confirmPswTxtField.getText());
            messagesLabel.setText(pswMatch ? "" : "The passwords don't match");
            submitBtn.setDisable(!pswMatch);
        }
    }

    public void onSubmitBtn(ActionEvent ignoredEvent) {
        // TODO: show psw prompt, login or register based on other inputs
        String username = usernameTxtField.getText();
        if (state == State.Start) {
            if (username.isEmpty()) return;
            boolean userExists = Boolean.parseBoolean(HttpHandler.get("/user/exists", new HashMap<>() {{
                put("username", username);
            }}));

            pswContainer.setVisible(true);
            confirmPswContainer.setVisible(!userExists);
            submitBtn.setText(userExists ? "Login" : "Register");
            state = userExists ? State.Login : State.Register;
            onTxtFieldChanged(null);
        } else {
            if (pswTxtField.getText().isEmpty()) return;
            // todo validate password
            String pswHash = Hashing.sha256().hashString(pswTxtField.getText(), StandardCharsets.UTF_8).toString();
            if (state == State.Register) {
                HttpHandler.post("/user/register", username + "," + pswHash);
                state = State.Login;
                confirmPswContainer.setVisible(false);
                submitBtn.setText("Login");
            } else if (state == State.Login) {
                boolean auth = Boolean.parseBoolean(HttpHandler.post("/user/auth", username + "," + pswHash));
                if (auth) {
                    messagesLabel.setText("Login successful");
                    // todo load main scene
                } else {
                    messagesLabel.setText("Wrong password. Try again");
                    pswTxtField.setText("");
                }
            }
            onTxtFieldChanged(null);
        }
    }
}
