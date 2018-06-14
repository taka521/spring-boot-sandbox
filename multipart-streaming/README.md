# multipart-streaming

マルチパートリクエストを、ストリーミング処理したかった。

## 環境

* `SpringMVC` + `commons fileupload`

## ざっくりと説明

* `ServletFileUpload` を使う。

```java
// マルチパートリクエストか判断
ServletFileUpload.isMultipartContent(HttpServletRequest)

// リクエストコンテキストから、マルチパートデータのイテレータを取得
final ServletFileUpload servletFileUpload = new ServletFileUpload();
final FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);
// イテレータから FileItemStream を取得して、ストリームを得る
final FileItemStream itemStream = fileItemIterator.next();
final InputStream is = itemStream.openStream();
```