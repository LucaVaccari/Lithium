package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import it.unibs.pajc.lithium.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);
        SceneManager.loadScene("/FXMLs/mainScene.fxml", this, true);
    }

    public static Gson getGson() {
        return gson;
    }
}
