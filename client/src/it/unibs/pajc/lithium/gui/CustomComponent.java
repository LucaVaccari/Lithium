package it.unibs.pajc.lithium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public abstract class CustomComponent extends AnchorPane {
    protected abstract String fxmlPath();

    public CustomComponent() {
        super();
        FXMLLoader loader = FXMLFileLoader.getLoader(fxmlPath());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
