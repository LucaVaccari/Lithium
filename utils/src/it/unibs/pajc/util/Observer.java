package it.unibs.pajc.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides a simple implementation of the observer pattern, with one argument and no return values
 */
public class Observer<T> {
    private final Set<NoReturnFunction<T>> listeners = ConcurrentHashMap.newKeySet();

    public void invoke(T param) {
        listeners.forEach(l -> l.apply(param));
    }

    public void addListener(NoReturnFunction<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(NoReturnFunction<T> listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }
}
