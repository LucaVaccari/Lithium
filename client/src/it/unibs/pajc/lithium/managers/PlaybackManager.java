package it.unibs.pajc.lithium.managers;

import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.util.Observer;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Stream;

public final class PlaybackManager {
    private static MediaPlayer mediaPlayer;
    private final static LinkedList<Track> trackQueue = new LinkedList<>();
    private final static Stack<Track> previouslyPlayedTracks = new Stack<>();
    private final static Observer<Track> update = new Observer<>();
    private static Track currentTrack;

    private static void playQueue() {
        if (mediaPlayer != null) mediaPlayer.stop();
        if (trackQueue.isEmpty()) {
            stopPlayback();
            return;
        }
        var track = trackQueue.pollFirst();
        String url = HttpHandler.buildUrl("audio/" + track.getAudioPath());
        mediaPlayer = new MediaPlayer(new Media(url));
        mediaPlayer.setStartTime(Duration.ZERO);
        mediaPlayer.setStopTime(Duration.seconds(track.getDuration()));
        mediaPlayer.setOnError(() -> {
            AlertUtil.showErrorAlert("Playback error", "Error during playback", mediaPlayer.getError().getMessage());
            update.invoke(track);
        });
        mediaPlayer.setOnPlaying(() -> update.invoke(track));
        mediaPlayer.setOnPaused(() -> update.invoke(track));
        mediaPlayer.setOnEndOfMedia(() -> {
            if (trackQueue.isEmpty()) stopPlayback();
            else playQueue();
            currentTrack = null;
            update.invoke(track);
        });
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            System.out.println("Playing track: " + track.getTitle());
            if (PartyManager.isHost()) PartyManager.sendCurrentTrack(track);
            currentTrack = track;
            previouslyPlayedTracks.push(track);
            update.invoke(track);
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
        if (trackQueue.isEmpty()) addToQueue(track);
        else trackQueue.addFirst(track);
        update.invoke(getCurentTrack());
    }

    public static void playNext(Track[] tracks) {
        if (trackQueue.isEmpty()) addToQueue(tracks);
        else trackQueue.addAll(0, List.of(tracks));
        update.invoke(getCurentTrack());
    }

    public static void addToQueue(Track track) {
        if (currentTrack == null) playImmediately(track);
        else trackQueue.addLast(track);
        update.invoke(getCurentTrack());
    }

    public static void addToQueue(Track[] tracks) {
        trackQueue.addAll(Stream.of(tracks).filter(Objects::nonNull).toList());
        if (mediaPlayer == null || !mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)) playQueue();
        update.invoke(getCurentTrack());
    }

    public static void togglePlay() {
        if (mediaPlayer == null) return;
        MediaPlayer.Status status = mediaPlayer.getStatus();
        if (status.equals(MediaPlayer.Status.PAUSED)) play();
        else if (status.equals(MediaPlayer.Status.PLAYING)) pause();
        else System.out.println("Current status: " + status);
    }

    public static void pause() {
        if (mediaPlayer == null) return;
        mediaPlayer.pause();
        if (PartyManager.isHost()) PartyManager.sendPause(true);
    }

    public static void play() {
        if (mediaPlayer == null) return;
        mediaPlayer.play();
        if (PartyManager.isHost()) PartyManager.sendPause(false);
    }

    public static void previousTrack() {
        if (previouslyPlayedTracks.isEmpty()) return;
        playNext(previouslyPlayedTracks.pop());
        if (previouslyPlayedTracks.isEmpty()) return;
        playImmediately(previouslyPlayedTracks.pop());
    }

    public static void nextTrack() {
        playQueue();
    }

    public static void removeFromQueue(Track track) {
        trackQueue.remove(track);
        update.invoke(getCurentTrack());
    }

    public static void seek(double time) {
        if (mediaPlayer == null) return;
        var seekDuration = Duration.seconds(time);
        Platform.runLater(() -> mediaPlayer.seek(mediaPlayer.getStartTime().add(seekDuration)));
        if (PartyManager.joinedAndHost()) PartyManager.sendSync(time);
    }

    public static void stopPlayback() {
        pause();
        trackQueue.clear();
        currentTrack = null;
        update.invoke(null);
        System.out.println("STop playback");
    }

    /**
     * @return the current playback time in seconds
     */
    public static double getCurrentTime() {
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

    public static Observer<Track> getUpdate() {
        return update;
    }

    public static Track getCurentTrack() {
        return currentTrack;
    }

    public static Set<Track> getTrackQueue() {
        var trackList = new LinkedHashSet<>(previouslyPlayedTracks);
        trackList.add(currentTrack);
        trackList.addAll(trackQueue);
        return trackList;
    }

    public static void clearQueue() {
        previouslyPlayedTracks.clear();
        trackQueue.clear();
        update.invoke(currentTrack);
    }

    public static void setVolume(double value) {
        if (mediaPlayer == null) return;
        mediaPlayer.setVolume(value);
    }

    private PlaybackManager() {
    }
}
