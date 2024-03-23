package it.unibs.pajc.lithium.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class SceneManager {
    // TODO put scene paths in a map
    private static Stage mainStage;

    private final static Stack<Scene> scenes = new Stack<>();
    private static Scene mainScene;

    public static void init(Stage mainStage) {
        SceneManager.mainStage = mainStage;
        mainStage.setTitle("Lithium");
        mainStage.setOnCloseRequest(e -> System.exit(0));
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void loadScene(String path, Object caller, boolean main) {
        var root = FXMLFileLoader.loadFXML(path, caller);
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        scenes.push(scene);
        mainStage.sizeToScene();
        if (main) mainScene = scene;
    }

    public static void loadScene(String path, Object caller) {
        loadScene(path, caller, false);
    }

    public static void loadMainScene(Object caller) {
        SceneManager.loadScene("/FXMLs/mainScene.fxml", caller, true);
    }

    public static void backToPreviousScene() {
        scenes.pop();
        mainStage.setScene(scenes.peek());
        mainStage.sizeToScene();
    }

    // TODO add home button to scenes
    public static void backToMainScene() {
        scenes.clear();
        mainStage.setScene(mainScene);
        mainStage.sizeToScene();
    }
}
