package it.unibs.pajc.lithium.gui;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kong.unirest.Unirest;

import java.util.Stack;

public class SceneManager {
    // TODO put scene paths in a map
    private static Stage mainStage;

    private final static Stack<Scene> scenes = new Stack<>();
    private static Scene mainScene;

    public static void init(Stage mainStage) {
        SceneManager.mainStage = mainStage;
        mainStage.setTitle("Lithium");
        mainStage.setOnCloseRequest(e -> {
            Unirest.shutDown();
            System.exit(0);
        });
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
        scenes.clear();
        SceneManager.loadScene("/FXMLs/mainScene.fxml", caller, true);
    }

    public static void backToPreviousScene() {
        if (scenes.size() > 1) {
            scenes.pop();
            mainStage.setScene(scenes.peek());
            mainStage.sizeToScene();
        } else {
            System.err.println("Not enough scenes in stack :(");
        }
    }

    public static void backToMainScene() {
        if (mainStage == null) return;
        scenes.clear();
        scenes.push(mainScene);
        mainStage.setScene(mainScene);
        mainStage.sizeToScene();
    }

    public static void openBlockingWindow(String title, String path, Object caller) {
        Stage newStage = new Stage();
        newStage.setScene(new Scene(FXMLFileLoader.loadFXML(path, caller)));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setResizable(false);
        newStage.sizeToScene();
        newStage.setTitle(title);
        newStage.showAndWait();
    }
}
