package it.unibs.pajc.lithium;

public final class Logger {
    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void logError(String msg) {
        System.err.println(msg);
    }
    // TODO replace all printStackTrack with logError
}
