package com.example.validation_demo.domain;

public class UserId extends Domain<Long> {

    private UserId(Long value) {
        super(value);
    }

    public static UserId of(final Long value) {
        return new UserId(value);
    }

    public static UserId of(final String value) {
        return new UserId(Long.valueOf(value));
    }

}
