package com.topperbibb.hacktcnj2021.client.game;

public class Triplet<F, S, T> {

    private final F first;
    private final S second;
    private final T third;

    public Triplet(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <F, S, T> Triplet<F, S, T> of(F first, S second, T third) {
        return new Triplet<F, S, T>(first, second, third);
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
