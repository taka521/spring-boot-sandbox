package com.example.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
    @Value による外部設定値の取得。
    フィールド、セッターメソッド、コンストラクタ引数の何れかに @Value{キー:初期値} の形式で記載する。
    ただし、この @Value による外部設定値の取得は、DIコンテナで管理されているクラスに限る。

    定義としては
        @Value{"${キー:デフォルト値}"}
    の形式。デフォルト値は省略可能だが、外部値として未定義の場合にはエラーになる。
    （UnsatisfiedDependencyException がスローされる）
 */
@Component
public class ApplicationValues {

    // フィールドに注釈する場合
    @Value("${app.host:localhost}")
    private String host;
    private int sessionTimeout;
    private int requestTimeout;

    // コンストラクタの引数に注釈する場合
    public ApplicationValues(@Value("${app.sessionTimeout:3000}") int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    // setterに注釈する場合
    @Value("${app.requestTimeout}")
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
}
