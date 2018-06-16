# multipart-streaming

マルチパートリクエストを、ストリーミング処理したかった。  
ついでに、AWS S3へのアップロードを可能にした。

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

取得したストリームを利用して、そのままS3アップロードするだけ。  
なお、

* 
* マルチパートアップロードでS3にUPすると、だいたい2分程度であがる。

## 注意点

* マルチパートアップロードでS3にアップロードする場合、`ObjectMetadata` に `Content-length` を指定しないとOutOfMemoryが発生する場合がある。
  * 特に大容量ファイルの場合は、絶対指定する必要がある。
  * 全体的なサイズが不明なため、一度メモリに展開しようとしているっぽい。
  * なので、事前に長さを知っておく必要がある。