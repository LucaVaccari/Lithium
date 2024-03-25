package it.unibs.pajc.util;

/**
 * Contains a function with no return values and one parameter
 *
 * @param <T> The parameter of the function contained
 */
public interface NoReturnFunction<T> {
    void apply(T param);
}
