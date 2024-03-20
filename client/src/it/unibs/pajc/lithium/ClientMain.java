package it.unibs.pajc.lithium;

import com.google.gson.Gson;
import it.unibs.pajc.lithium.gui.FXMLFileLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private static Stage mainStage;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        var root = FXMLFileLoader.loadFXML("/FXMLs/mainScene.fxml", this);
        Scene scene = new Scene(root);
        mainStage.setScene(scene);

        mainStage.setTitle("Lithium");
        mainStage.setOnCloseRequest(e -> System.exit(0));
        mainStage.sizeToScene();
        mainStage.setResizable(true);
        mainStage.show();
    }

    public static void setScene(Scene scene) {
        mainStage.setScene(scene);
    }

    public static Gson getGson() {
        return gson;
    }
}
