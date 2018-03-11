package com.example.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
    Type-safe Configuration Propertiesを利用した外部設定値の取得。
    設定値を注入したいクラスに @ConfigurationProperties を注釈する。
    prefix属性を設定すると、prefix + .フィールド名 の形式で定義した外部設定値を取得して注入してくれる。

    デフォルト値を指定したい場合には、フィールドに初期値を指定する。
 */
@Component
@ConfigurationProperties(prefix = "app.database")
public class DatabaseSettings {

    private String host = "dummyHost";         // app.database.host の値が注入
    private String username = "dummyUsername"; // app.database.username の値が注入
    private String password = "dummyPassword"; // app.database.password の値が注入

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
