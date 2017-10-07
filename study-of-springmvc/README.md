# Study of Spring MVC

Spring MVCの勉強用プロジェクト。  
このプロジェクトでは、Spring Boot に頼らず設定〜デプロイまでを自前で行います。

勉強した祭のメモなどは、このファイルに追記していきます。


## メモ

### `web.xml` のテンプレ

以下のサイトに、Servlet2.3 ~ 3.1までまとめてあった。

* [web.xmlのバージョン別DTD・XSDの宣言方法](http://kokuzawa.github.io/blog/2015/04/07/web-dot-xmlfalsebaziyonbie-dtdxsdfalsexuan-yan-fang-fa/)


## `@EnableWebMvc` の役割

`@EnableWebMvc`アノテーションは、Spring MVCが提供しているコンフィグレーションクラスをインポートし、
Spring MVCを利用するために必要となるコンポーネントのBean定義が自動で行われる。

`@EnableWebMvc`の中身は以下のようになっている。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}
```
 




## 困ったこととか


### 依存関係に `provided 'avax.servlet:javax.servlet-api:3.0.1` を追加するとエラー

依存関係に `provided 'avax.servlet:javax.servlet-api:3.0.1` を追加したら、以下のエラーが出た。

```
* Where:
Build file '/Users/takaaki/IdeaProjects/spring-boot-sandbox/study-of-springmvc/build.gradle' line: 17
  
* What went wrong:
A problem occurred evaluating root project 'study-of-springmvc'.
> Could not find method provided() for arguments [javax.servlet:javax.servlet-api:3.0.1] on object of type org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler.
  
* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.
```

`provided` が無いらしい?  
代わりに、`compleOnly` を指定すればエラー吐かなくなった。

* [gradle エラー Could not find method provided() for arguments が発生する](https://www.monotalk.xyz/blog/gradle-could-not-find-method-provided-for-arguments/)


