package com.example.external;

public final class Console {

    private Console(){}

    static void log(final String key, final Object value){
        System.out.printf("%s : %s%n", key, value.toString());
    }
}
