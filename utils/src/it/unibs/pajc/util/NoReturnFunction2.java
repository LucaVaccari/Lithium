package it.unibs.pajc.util;

/**
 * Contains a function with no return values and two parameters
 *
 * @param <T> The first parameter of the function contained
 * @param <U> The second parameter of the function contained
 */
public interface NoReturnFunction2<T, U> {
    void apply(T param1, U param2);
}
