package it.unibs.pajc.lithium.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage mainStage;
    public static Scene mainScene;

    public static void init(Stage mainStage) {
        SceneManager.mainStage = mainStage;
        mainStage.setTitle("Lithium");
        mainStage.setOnCloseRequest(e -> System.exit(0));
        mainStage.setResizable(true);
        mainStage.show();
    }

    public static void loadScene(String path, Object caller, boolean isMainScene) {
        var root = FXMLFileLoader.loadFXML(path, caller);
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        if (isMainScene) mainScene = scene;
    }

    public static void backToMainScene() {
        if (mainScene != null) mainStage.setScene(mainScene);
    }
}
