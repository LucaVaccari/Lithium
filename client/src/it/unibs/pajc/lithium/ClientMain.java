package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.gui.FXMLFileLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        var root = FXMLFileLoader.loadFXML("/FXMLs/login.fxml", this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.sizeToScene();
        stage.setResizable(true);
        stage.show();
    }
}
