package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private static User user;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);
        SceneManager.loadMainScene(this);
    }

    public static User getUser() {
        // TODO: this is temp
        if (user == null) user = ItemProvider.getItem(1, User.class);
        return user;
    }
}
