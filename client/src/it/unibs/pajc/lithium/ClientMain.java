package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.gui.SceneManager;
import it.unibs.pajc.lithium.managers.AccountManager;
import it.unibs.pajc.lithium.managers.LcpManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

    private static final LcpManager connectionManager = new LcpManager();

    public static void main(String[] args) {
        new Thread(connectionManager).start();
        launch(args);
    }

    public static LcpManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);
        if (AccountManager.authenticateUserFromSavedInfo()) {
            SceneManager.loadMainScene(this);
            return;
        }
        SceneManager.loadScene("/FXMLs/login.fxml", this);
    }
}
