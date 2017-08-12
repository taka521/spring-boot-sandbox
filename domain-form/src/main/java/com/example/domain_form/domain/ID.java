package com.example.domain_form.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Long.class, factoryMethod = "of")
public class ID<E> {

    private final Long value;

    private ID(final Long value) {
        this.value = value;
    }

    public static <R> ID<R> of(final Long value){
        return new ID<>(value);
    }

    public static <R> ID<R> of(final String value){
        return new ID<>(Long.valueOf(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Long getValue() {
        return value;
    }
}
