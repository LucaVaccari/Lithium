package it.unibs.pajc.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides a simple implementation of the observer pattern, with one argument and no return values
 */
public class Observer2<T, U> {
    private final Set<NoReturnFunction2<T, U>> listeners = ConcurrentHashMap.newKeySet();

    public void invoke(T param1, U param2) {
        listeners.forEach(l -> l.apply(param1, param2));
    }

    public void addListener(NoReturnFunction2<T, U> listener) {
        listeners.add(listener);
    }

    public void removeListener(NoReturnFunction2<T, U> listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }
}
