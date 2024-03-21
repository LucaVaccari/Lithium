package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.db.om.Track;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public final class PlaybackManager {
    private static Media currentMedia;
    private static MediaPlayer mediaPlayer;
    private final static LinkedList<Track> trackQueue = new LinkedList<>();
    private final static Stack<Track> previouslyPlayedTracks = new Stack<>();

    private static void playQueue() {
        if (trackQueue.isEmpty()) return;
        var track = trackQueue.pollFirst();
        previouslyPlayedTracks.push(track);
        currentMedia = new Media(track.getAudioPath());
        mediaPlayer = new MediaPlayer(currentMedia);
        currentMedia.setOnError(() -> {
            System.out.println("Errore di riproduzione");

        });
        mediaPlayer.setOnReady(() -> {
            System.out.println(currentMedia.getDuration().toSeconds());
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            if (trackQueue.isEmpty()) stopPlayback();
            else playQueue();
        });
        mediaPlayer.play();
    }

    public static void playImmediately(Track track) {
        trackQueue.addFirst(track);
        playQueue();
    }

    public static void playImmediately(Track[] tracks) {
        trackQueue.addAll(0, List.of(tracks));
        playQueue();
    }

    public static void playNext(Track track) {
        if (trackQueue.isEmpty()) playImmediately(track);
        else trackQueue.add(1, track);
        playQueue();
    }

    public static void playNext(Track[] tracks) {
        if (trackQueue.isEmpty()) addToQueue(tracks);
        else trackQueue.addAll(1, List.of(tracks));
        playQueue();
    }

    public static void addToQueue(Track track) {
        if (trackQueue.isEmpty()) playImmediately(track);
        else trackQueue.addLast(track);
        playQueue();
    }

    public static void addToQueue(Track[] tracks) {
        trackQueue.addAll(List.of(tracks));
    }

    public static void togglePlay() {
        if (mediaPlayer != null && !trackQueue.isEmpty()) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status.equals(MediaPlayer.Status.PAUSED)) mediaPlayer.play();
            else if (status.equals(MediaPlayer.Status.PLAYING)) mediaPlayer.pause();
            else System.out.println("Current status: " + status);
        }
    }

    public static void pause() {
        if (mediaPlayer != null && !trackQueue.isEmpty()) mediaPlayer.pause();
    }

    public static void previousTrack() {
        if (previouslyPlayedTracks.isEmpty()) return;
        playImmediately(previouslyPlayedTracks.pop());
    }

    public static void nextTrack() {
        playQueue();
    }

    public static void stopPlayback() {
        pause();
        trackQueue.clear();
    }

    public static int getCurrentTime() {
        return mediaPlayer != null ? (int) mediaPlayer.getCurrentTime().toSeconds() : 0;
    }

    public static double getPlayPercentage() {
        if (mediaPlayer == null) return 0;
        var currentSeconds = mediaPlayer.getCurrentTime().toSeconds();
        var totalSeconds = mediaPlayer.getMedia().getDuration().toSeconds();
        return currentSeconds / totalSeconds;
    }

    private PlaybackManager() {
    }
}
