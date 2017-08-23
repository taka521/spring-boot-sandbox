package com.example.validation_demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserId {

    private final Long value;

    public static UserId of(final String value) {
        return new UserId(Long.valueOf(value));
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
