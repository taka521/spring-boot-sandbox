package com.example.domain_form.domain;

import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of")
public class Name<E> {

    private final String value;

    private Name(final String value) {
        this.value = value;
    }

    public static <R> Name<R> of(final String value){
        return new Name<>(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
