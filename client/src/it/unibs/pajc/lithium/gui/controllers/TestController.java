package it.unibs.pajc.lithium.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class TestController {
    @FXML
    private MediaView mediaView;
    
    @FXML
    private void initialize() {
        // TEST
        Media testMedia = new Media("http://localhost:8080/test");
        MediaPlayer player = new MediaPlayer(testMedia);
        mediaView.setMediaPlayer(player);

        testMedia.setOnError(() -> System.out.println("Errore"));
        player.setOnReady(() -> {
            System.out.println("SUS");
            System.out.println(testMedia.getDuration().toSeconds());
        });
        player.play();
    }
}
