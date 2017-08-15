# Spring Boot + Spring MVCでエラーハンドリング

何でもそうだが、エラーハンドリングが必須である。<br/>
Spring Boot（SpringMVC）でエラーハンドリングするためのサンプルプロジェクト。

## 参考サイト

* [Spring Boot で Boot した後に作る Web アプリケーション基盤](https://speakerdeck.com/sinsengumi/spring-boot-application-infrastructure)
  * 大部分（というか、ほとんど）はこのスライドを参考にした。
* [Spring MVC(+ Spring Boot) における404時の動き](http://javazuki.com/articles/spring-mvc-and-boot-404-process.html)
  * 上記通りに実装した場合、404の発生時に例外（`Throwable`）が null となる。（うまく例外が伝達できてない？）
  * デフォルトだと「/**」のマッピングが有効になっているので、そのマッピングを外せば上手くいった。（要調査）
* [ErrorControllerにはPOSTの処理も必要 - Qiita](http://qiita.com/yakumo/items/91b6c818cbfabcb7cfb0)
  * この記事ではPOST定義が必要、と書いてあるが自分の環境では発生していない。