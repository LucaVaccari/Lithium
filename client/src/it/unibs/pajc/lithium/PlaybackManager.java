package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.db.om.Track;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.LinkedList;

public final class PlaybackManager {
    private static Media currentMedia;
    private static MediaPlayer mediaPlayer;
    private final static LinkedList<Track> trackQueue = new LinkedList<>();

    private static void playQueue() {
        if (trackQueue.isEmpty()) return;
        currentMedia = new Media(trackQueue.pollFirst().getAudioPath());
        mediaPlayer = new MediaPlayer(currentMedia);
        currentMedia.setOnError(() -> System.out.println("Errore di riproduzione"));
        mediaPlayer.setOnReady(() -> {
            System.out.println(currentMedia.getDuration().toSeconds());
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            // TODO: play next song
        });
        mediaPlayer.play();
    }

    public static void playMediaImmediately(Track track) {
        trackQueue.addFirst(track);
        playQueue();
    }

    public static void playNext(Track track) {
        if (trackQueue.isEmpty()) playMediaImmediately(track);
        else trackQueue.add(1, track);
        playQueue();
    }

    public static void addToQueue(Track track) {
        if (trackQueue.isEmpty()) playMediaImmediately(track);
        else trackQueue.addLast(track);
        playQueue();
    }

    public static void play() {
        if (mediaPlayer != null && !trackQueue.isEmpty()) mediaPlayer.play();
    }

    public static void pause() {
        if (mediaPlayer != null && !trackQueue.isEmpty()) mediaPlayer.pause();
    }

    private PlaybackManager() {
    }
}
