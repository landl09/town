package com.lan.town;

/**
 * Process unit definition.
 * Commonly, it receives input, process it, then send the result out
 *
 * @param <I>
 * @param <O>
 */
public interface Unit<I, O> {

    I receive(I input);

    O process(I input);

    void send(O output);

    default void call(I input) {
        send(process(receive(input)));
    }

}