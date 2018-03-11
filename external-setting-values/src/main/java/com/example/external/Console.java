package com.example.external;

public final class Console {

    private Console(){}

    static <T> void startLog(final Class<T> clazz){
        System.out.println("========================");
        System.out.printf("%s%n", clazz.getSimpleName());
        System.out.println("========================");
    }

    static void log(final String key, final Object value){
        System.out.printf("%s : %s%n", key, value.toString());
    }

    static void endLog(){
        System.out.printf("========================%n%n");
    }
}
