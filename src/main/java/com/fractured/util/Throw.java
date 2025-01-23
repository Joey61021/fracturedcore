package com.fractured.util;

public final class Throw {
    private Throw()
    {
    }

    public interface Consumer<P, E extends Throwable> {
        void accept(P parameter) throws E;
    }

    public interface Function<R, P, E extends Throwable> {
        R apply(P parameter) throws E;
    }
}