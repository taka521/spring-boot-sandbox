package com.example.validation_demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class Password {

    @Pattern(regexp = "^[0-9a-zA-Z]{4,10}$")
    private final String value;

    @Override
    public String toString(){
        return value;
    }
}
