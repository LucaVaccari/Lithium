package it.unibs.pajc.util;

import java.util.ArrayList;

/**
 * Provides a simple implementation of the observer pattern, with no arguments and no return values
 */
public class Observer {
    private final ArrayList<Runnable> listeners = new ArrayList<>();

    public void invoke() {
        listeners.forEach(Runnable::run);
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }
}
