package it.unibs.pajc.lithium.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class SceneManager {
    private static Stage mainStage;

    private final static Stack<Scene> scenes = new Stack<>();

    public static void init(Stage mainStage) {
        SceneManager.mainStage = mainStage;
        mainStage.setTitle("Lithium");
        mainStage.setOnCloseRequest(e -> System.exit(0));
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void loadScene(String path, Object caller) {
        var root = FXMLFileLoader.loadFXML(path, caller);
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        scenes.push(scene);
        mainStage.sizeToScene();
    }

    public static void backToPreviousScene() {
        scenes.pop();
        mainStage.setScene(scenes.peek());
        mainStage.sizeToScene();
    }

    // TODO: back to main scene
}
