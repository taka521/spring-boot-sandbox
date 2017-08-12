# DomainクラスをFormクラスのFieldとして使用する

## やりたいこと

Domaのドメインクラスを、Formクラスのフィールドとして使用することができるか検証する。

## 結果

できる。

## どうすればいいか

ドメインクラスに、Stringを引数にとるファクトリメソッド、またはコンストラクタを定義する。

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

こうすることで、Spring側がファクトリメソッドを呼び出してインスタンス化してくれる。