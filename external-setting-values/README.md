# 外部設定値の調査用プロジェクト

Springにおける外部設定値について検証するためのプロジェクト

## 参考

* [Spring Bootの外部設定値の扱い方を理解する - Qiita](https://qiita.com/kazuki43zoo/items/0ce92fce6d6f3b7bf8eb)


## 外部設定値について

上記のQiita記事によると、Springで外部設定値を参照する方法は以下の３つ。


> * Spring Frameworkが提供している`@Value`を使用する
> * Spring Bootが提供している「[Type-safe Configuration Properties](http://docs.spring.io/spring-boot/docs/1.4.1.BUILD-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-typesafe-configuration-properties)」の仕組みを利用する （**この仕組みを使うのがお勧めです** :laughing: :laughing: :laughing:）
> * Spring Frameworkが提供している`Environment`インタフェースを利用する。

また、外部設定値の指定については複数あるよう。

> 以下に、外部設定値の主な指定方法を(優先度が高い順に)紹介します。なお、個人的にたぶん利用しないだろうな〜と思った方法については説明は割愛します。（サポートしている指定方法の全量は、[Spring Bootのリファレンス](http://docs.spring.io/spring-boot/docs/1.4.1.BUILD-SNAPSHOT/reference/htmlsingle/#boot-features-external-config)をご覧ください）
> 
> | 指定方法 | 説明 |
> | --- | --- |
> | Developer tools用のプロパティファイル | [Spring Boot Developer tools](http://docs.spring.io/spring-boot/docs/1.4.1.BUILD-SNAPSHOT/reference/htmlsingle/#using-boot-devtools)用のプロパティファイル(`~/.spring-boot-devtools.properties`)に「`設定名=値`」の形式で指定する。（Spring Boot Developer toolsを適用している場合のみ） |
> | `@SpringBootTest` | `@SpringBootTest`の`properties`属性に「`設定名=値`」の形式で指定する。テストケース内で設定値を指定したい場合に利用。 （ただし、後述の`@TestPropertySource`の`properties`属性に同じ設定値があると、`@TestPropertySource`の値が有効となる。ってかそんな使い方しないと思いますが・・・ :sweat_smile: ） |
> | `@TestPropertySource` | `@TestPropertySource`（テスト用のプロパティファイル or アノテーションの属性値）に「`設定名=値`」の形式で指定する。テストケース内で設定値を指定したい場合に利用。 |
> | コマンドライン引数 | コマンドライン引数に「`--設定名=値`」の形式で指定する。 | |
> | JNDIリソース | 「`java:comp/env/設定名`」の形式で取得可能なJNDIリソースとして指定する。主にwarファイルをアプリケーションサーバにデプロイする場合に利用。| 
> | JVMのシステムプロパティ | JVMの起動引数に「`-D設定名=値`」の形式で指定する。| 
> | OSの環境変数 | OSの環境変数に「`設定名=値`」の形式で指定する。|
> | Spring Boot専用のプロパティファイル | Spring Boot専用のプロパティファイル/YAMLファイルに「`設定名=値`」の形式で指定する。 |
> | `@PropertySource` | `@PropertySource`に指定した任意のプロパティファイルに「`設定名=値`」の形式で指定する。 |

とりあえず、試してみる。