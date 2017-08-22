# Spring Boot + Spring MVC でメッセージ出力する。

## やりたいこと

タイトル通り。<br/>
外部ファイルに定義したメッセージを、ControllerやViewで使用する。

## メッセージファイルの作成

`main/resources` 配下に、`messages.properties` ファイルを作成して `[キー]=[メッセージ]` の形式でメッセージを定義する。

```properties
msg001=メッセージです。
msg002=こんにちは、{0}さん。
```

メッセージに動的な値を埋め込みたい場合には、上記のように `{数字}` をメッセージに定義する。


## 使用する

メッセージを使用する箇所としては、`@Controller` や `@Component` が注釈されたクラスか、
Thymeleafなどのビュー側のどちらかになる。

### コントローラなどで利用する場合

`org.springframework.context.MessageSource` を使用する。<br/>
文字で書くよりコード見たほうが早い。

```java
@Autowired
private MessageSource messageSource;

@RequestMapping("/")
public String sample(Model model, Locale locale){
    
    // getMessageメソッドの第一引数にキー、第二引数に埋め込む値、第三引数が Locale。
    model.addAttribute("msg001", messageSource.getMessage("MSG0001", null, locale));
    
    return "sample";
}
```

`MessageSource` をフィールドに定義し、 `@Autowired` 等でインスタンスを設定する。<br/>
余談だが、ハンドラメソッドの引数で `MessageSource` を受け取れるかと思って試してみたが、`BeanInstantiationException` が発生した。<br/>

```text
org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.context.MessageSource]: Specified class is an interface
	at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:99) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.method.annotation.ModelAttributeMethodProcessor.createAttribute(ModelAttributeMethodProcessor.java:141) ~[spring-web-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor.createAttribute(ServletModelAttributeMethodProcessor.java:81) ~[spring-webmvc-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.method.annotation.ModelAttributeMethodProcessor.resolveArgument(ModelAttributeMethodProcessor.java:101) ~[spring-web-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:121) ~[spring-web-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:158) ~[spring-web-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:128) ~[spring-web-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97) ~[spring-webmvc-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827) ~[spring-webmvc-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738) ~[spring-webmvc-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85) ~[spring-webmvc-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967) ~[spring-webmvc-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.10.RELEASE.jar:4.3.10.RELEASE]
    ...
```

### Thymeleafから直接メッセージを参照する場合

`#{キー}` で取得する。

```html
<p th:text="#{MSG001}"></p>

// 埋め込み変数が必要な場合には、キーの後ろに()をつけて、その中に変数を定義する。
<p th:text="#{MSG002(${username})}"></p>
```
