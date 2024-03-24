package it.unibs.pajc.lithium;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.util.Observer;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public final class PlaybackManager {
    private static MediaPlayer mediaPlayer;
    private final static LinkedList<Track> trackQueue = new LinkedList<>();
    private final static Stack<Track> previouslyPlayedTracks = new Stack<>();
    private final static Observer update = new Observer();

    private static void playQueue() {
        if (mediaPlayer != null) mediaPlayer.stop();
        if (trackQueue.isEmpty()) return;
        var track = trackQueue.pollFirst();
        previouslyPlayedTracks.push(track);
        String url = HttpHandler.buildUrl("audio/" + track.getAudioPath());
        mediaPlayer = new MediaPlayer(new Media(url));
        mediaPlayer.setStartTime(Duration.ZERO);
        mediaPlayer.setStopTime(Duration.seconds(track.getDuration()));
        mediaPlayer.setOnError(() -> {
            AlertUtil.showErrorAlert("Playback error", "Error during playback", mediaPlayer.getError().getMessage());
            update.invoke();
        });
        mediaPlayer.setOnPlaying(update::invoke);
        mediaPlayer.setOnPaused(update::invoke);
        mediaPlayer.setOnEndOfMedia(() -> {
            if (trackQueue.isEmpty()) stopPlayback();
            else playQueue();
        });
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            System.out.println("Playing track: " + track.getTitle());
            update.invoke();
        });
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
        else trackQueue.addFirst(track);
        update.invoke();
    }

    public static void playNext(Track[] tracks) {
        if (trackQueue.isEmpty()) addToQueue(tracks);
        else trackQueue.addAll(0, List.of(tracks));
        update.invoke();
    }

    public static void addToQueue(Track track) {
        if (trackQueue.isEmpty()) playImmediately(track);
        else trackQueue.addLast(track);
        update.invoke();
    }

    public static void addToQueue(Track[] tracks) {
        trackQueue.addAll(List.of(tracks));
    }

    public static void togglePlay() {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status.equals(MediaPlayer.Status.PAUSED)) mediaPlayer.play();
            else if (status.equals(MediaPlayer.Status.PLAYING)) mediaPlayer.pause();
            else System.out.println("Current status: " + status);
        }
    }

    public static void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public static void previousTrack() {
        if (previouslyPlayedTracks.isEmpty()) return;
        playImmediately(previouslyPlayedTracks.pop());
    }

    public static void nextTrack() {
        playQueue();
    }

    public static void seek(double time) {
        if (mediaPlayer == null) return;
        var seekDuration = Duration.seconds(time);
        Platform.runLater(() -> mediaPlayer.seek(mediaPlayer.getStartTime().add(seekDuration)));
    }

    public static void stopPlayback() {
        pause();
        trackQueue.clear();
        update.invoke();
    }

    public static int getCurrentTime() {
        return mediaPlayer != null ? (int) mediaPlayer.getCurrentTime().toSeconds() : 0;
    }

    public static double getMaxTime() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getStopTime().subtract(mediaPlayer.getStartTime()).toSeconds();
    }

    public static boolean isPlaying() {
        if (mediaPlayer == null) return false;
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }

    public static Observer getUpdate() {
        return update;
    }

    public static Track getCurentTrack() {
        // TODO fix
        return previouslyPlayedTracks.isEmpty() ? null : previouslyPlayedTracks.peek();
    }

    private PlaybackManager() {
    }
}
