package com.example.demo.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Site {

    API_INTERNAL("API(外向き)", "/api/internal"),
    API_EXTERNAL("API(外向き)", "/api"),
    SCREEN_ADMIN("管理画面", "/admin"),
    SCREEN_USER("ユーザ画面", "/");

    private final String label;
    private final String baseUrl;

    public static Site of(final String url) {
        if (url == null) {
            return SCREEN_USER;
        }

        for (Site site : values()) {
            if (url.startsWith(site.baseUrl)) {
                return site;
            }
        }

        return SCREEN_USER;
    }
}
