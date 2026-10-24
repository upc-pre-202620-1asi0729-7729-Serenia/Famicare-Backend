package com.acme.familarebackend.shared.application.result;

import java.util.Optional;
import java.util.function.Function;

public sealed interface Result<T, E> permits Result.Logrado, Result.Fallido {
    //COMPLETADO
    static <T,E> Result<T, E> logrado(T value) { return new Logrado<>(value); }
    //ERROR
    static <T,E> Result<T, E> fallido(E error) { return new Fallido<>(error); }

    record Logrado<T,E> (T value) implements Result<T, E> {}
    record Fallido<T,E> (E error) implements Result<T, E> {}

    default boolean isLogrado() { return this instanceof Result.Logrado<?, ?>; }
    default boolean isFallido() { return this instanceof Result.Fallido<?, ?>; }

    default Optional<E> fallido() {
        if (this instanceof Fallido<?, ?> falla) {
            @SuppressWarnings("unchecked")
            E error = ((Fallido<T, E>) falla).error();
            return Optional.of(error);
        }
        return Optional.empty();
    }

    default Optional<T> logrado() {
        if (this instanceof Logrado<?, ?> logro) {
            @SuppressWarnings("unchecked")
            T value = ((Logrado<T, E>) logro).value();
            return Optional.of(value);
        }
        return Optional.empty();
    }

    default <R> R fold(Function<? super T, ? extends R> onSuccess, Function<? super E, ? extends R> onFailure) {
        if (this instanceof Logrado<?,?> logro) {
            @SuppressWarnings("unchecked")
            T value = ((Logrado<T,E>) logro).value();
            return onSuccess.apply(value);
        }
        E error = ((Fallido<T,E>) this).error();
        return onFailure.apply(error);
    }
}
