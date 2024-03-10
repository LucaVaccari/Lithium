package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.gui.FXMLFileLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        var root = FXMLFileLoader.loadFXML("/FXMLs/mainScene.fxml", this);
        Scene scene = new Scene(root);
        mainStage.setScene(scene);

        mainStage.setOnCloseRequest(e -> System.exit(0));
        mainStage.sizeToScene();
        mainStage.setResizable(true);
        mainStage.show();
    }

    public static void setScene(Scene scene) {
        mainStage.setScene(scene);
    }
}
