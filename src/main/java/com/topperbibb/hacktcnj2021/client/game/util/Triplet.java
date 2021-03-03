package com.topperbibb.hacktcnj2021.client.game.util;

/**
 * A simple datatype that holds three associated values
 * @param <F> any type, for the first element of the Triplet
 * @param <S> any type, for the second element of the Triplet
 * @param <T> any type, for the third element of the Triplet
 */
public class Triplet<F, S, T> {

    private final F first;
    private final S second;
    private final T third;

    public Triplet(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Statically created Triplet with proper types
     * @param first the first element to place in the Triplet
     * @param second the second element to place in the Triplet
     * @param third the third element to place in the Triplet
     * @param <F> any type, for the first element of the Triplet
     * @param <S> any type, for the second element of the Triplet
     * @param <T> any type, for the third element of the Triplet
     * @return a newly instantiated Triplet with the values {@code first}, {@code second}, and {@code third}
     */
    public static <F, S, T> Triplet<F, S, T> of(F first, S second, T third) {
        return new Triplet<>(first, second, third);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

}
