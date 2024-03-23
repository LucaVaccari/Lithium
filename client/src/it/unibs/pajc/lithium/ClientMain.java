package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);
        if (AccountManager.authenticateUserFromSavedInfo()) {
            SceneManager.loadMainScene(this);
            return;
        }
        SceneManager.loadScene("/FXMLs/login.fxml", this);
//        SceneManager.loadMainScene(this);
    }
}
