package com.example.validation_demo.domain;

public class Password extends Domain<String> {

    private Password(String value) {
        super(value);
    }

    public static Password of(final String value){
        return new Password(value);
    }
}
