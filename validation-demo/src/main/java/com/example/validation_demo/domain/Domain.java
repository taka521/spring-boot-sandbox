package com.example.validation_demo.domain;

import java.io.Serializable;

public abstract class Domain<V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final V value;

    public Domain(final V value) {
        this.value = value;
    }

    public V getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
