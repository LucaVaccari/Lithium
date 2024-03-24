package it.unibs.pajc.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides a simple implementation of the observer pattern, with no arguments and no return values
 */
public class Observer {
    private final Set<Runnable> listeners = ConcurrentHashMap.newKeySet();

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
