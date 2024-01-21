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
        Media testMedia = new Media("https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8");
        MediaPlayer player = new MediaPlayer(testMedia);
        mediaView.setMediaPlayer(player);
        mediaView.setViewport(mediaView.getViewport());
        player.play();
    }
}
