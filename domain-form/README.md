# DomainクラスをFormクラスのFieldとして使用する

## やりたいこと

Domaのドメインクラスを、Formクラスのフィールドとして使用することができるか検証する。

## 結果

できる。

## どうすればいいか

ドメインクラスに、Stringを引数にとるpublic staticなファクトリメソッド、またはコンストラクタを定義する。

```java
@Domain(valueType = Long.class, factoryMethod = "of")
public class ID<E> {
    private final Long value;
    
    private ID(final Long value){
        this.value = value;
    }
    
    public static <R> ID<R> of(final Long value){
        return new ID<>(value);
    }
    
    // String を引数にとるファクトリメソッドを定義する。
    public static <R> ID<R> of(final String value){
        return new ID<>(Long.valueOf(value));
    }
    
    public Long getValue(){
        return this.value;
    }
}
```

こうすることで、Spring側がファクトリメソッドを呼び出してインスタンス化してくれる。<br/>
ただし、ファクトリメソッドの場合、メソッド名は `valueOf`, `of`, `from` の何れかである必要がある。（うらがみさん指摘）<br/>

* [ObjectToObjectConverter.determineFactoryMethod](https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/convert/support/ObjectToObjectConverter.java#L180)

## publicなフィールドに直接バインドする場合

`DataBinder.initDirectFieldAccess` を使用することで、getter/setter 無しのフィールドにバインドしてくれる。<br/>
うらがみさんがサンプルソースを作成してくださった。

* https://github.com/backpaper0/spring-boot-sandbox/commit/41bacf25aefa0b7e4f581a33c7b918eef7396342

```java
@ControllerAdvice
public class MyAdvice {
    @InitBinder
    void init(final WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
```

