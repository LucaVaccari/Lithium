package it.unibs.pajc.lithium;

public final class Logger {
    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void logError(Object caller, Exception e) {
        String name = caller != null ? caller.getClass().getName() : "null";
        System.err.println(name + ": " + e.getMessage());
    }
}
