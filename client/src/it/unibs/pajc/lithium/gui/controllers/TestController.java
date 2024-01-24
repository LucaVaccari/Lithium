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
        //Media testMedia = new Media("https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8");
        Media testMedia = new Media("http://localhost:8080/test");
        //Media testMedia = new Media("https://mtoczko.github.io/hls-test-streams/test-vtt-ts-segments/playlist.m3u8");
        //Media testMedia = new Media("http://localhost:8080/audio/syr001.ts");
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
