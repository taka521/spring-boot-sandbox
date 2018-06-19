# AWS S3の操作を検証するプロジェクト

## 概要

* AWS S3 に関する操作を検証するためのプロジェクト。
* SpringBoot（SpringMVC）の上に構築する。


## アップロード

アップロードには単純なアップロード（以下、シングルアップロード）と、マルチパートアップロードがある。  
※「シングルアップロード」は、単に自分がそう読んでいるだけで正式名称は知らない。  

シングルアップロードは、比較的サイズの小さいファイルをUPする場合に使用する。

```java
AmazonS3 s3 = ...;
PutObjectRequest putObject = new PutObjectRequest("バケット名", "キー（ファイル名）", File, metadata);
s3.putObject(putObject);
```

マルチパートアップロードは、比較的容量が大きい(ファイルサイズが100MBを超えるような)ファイルをアップロードする場合に有効なアップロード方法。  
アップロード対象となるファイルを分割して、並列でアップロードを行う。（最終的にはS3側で１つにする）  
なお、マルチアップロードを行う際のファイルサイズは**最低でも5MB**でなければならない。
また、**アップロードファイルのデータソースにストリームを使用している場合は、マルチパートアップロードは行われない。**

* [AWS SDK for Java の使用](https://docs.aws.amazon.com/ja_jp/AmazonS3/latest/dev/UsingTheMPJavaAPI.html#TestingJavaSamples)

> データのソースにストリームを使っている場合、TransferManager クラスは同時アップロードを実行しません。

* [マルチパートアップロードの概要](https://docs.aws.amazon.com/ja_jp/AmazonS3/latest/dev/mpuoverview.html)
* [Multipart Upload API を使用したオブジェクトのアップロード](https://docs.aws.amazon.com/ja_jp/AmazonS3/latest/dev/uploadobjusingmpu.html)

事前にファイルサイズが分かっている場合には高レベルAPIが使用できる `com.amazonaws.services.s3.transfer.TransferManager` を使用する。  
`TransferManager`の生成には、`TransferManagerBuilder` を使用して必要な設定などを行う。 

* [AWS Java SDK を使用したマルチパートアップロード (高レベル API)](https://docs.aws.amazon.com/ja_jp/AmazonS3/latest/dev/usingHLmpuJava.html)

```java
AmazonS3 s3 = ...;
TransferManager transferManager = TransferManagerBuilder.standard()
                    .withS3Client(s3)
                    .withMultipartUploadThreshold(multipartUploadThreshold)
                    .withMinimumUploadPartSize(minimumUploadPartSize)
                    .withExecutorFactory(() -> Executors.newFixedThreadPool(4))
                    .build();
Upload upload = transferManager.upload(putObjectRequest);
upload.waitForUploadResult();
```

* `withS3Client`
  * アップロードを行う際のS3クライアントを設定
* `withMultipartUploadThreshold`
  * マルチアップロードを行うか否かを判断する閾値
  * ここで設定した値を超える場合には、マルチパートアップロードが行われる。
* `withMinimumUploadPartSize`
  * マルチパートアップロード時の、分割サイズ。
  * 設定する値が小さすぎると、逆に遅くなってしまう可能性がある。
* `withExecutorFactory`
  * マルチパートアップロード時に使用する、`ExecutorService`の生成処理を設定。

なお、これらの設定についてはデフォルト値があり、
マルチパートアップロードに関する設定値は `TransferManagerConfiguration` 、
`ExecutorService` の生成については `TransferManagerUtils#createDefaultExecutorService()` を参照

> ```java
> public class TransferManagerConfiguration {
> 
>     /** Default minimum part size for upload parts. */
>     @SdkTestInternalApi
>     static final int DEFAULT_MINIMUM_UPLOAD_PART_SIZE = 5 * MB;
> 
>     /** Default size threshold for when to use multipart uploads.  */
>     @SdkTestInternalApi
>     static final long DEFAULT_MULTIPART_UPLOAD_THRESHOLD = 16 * MB;
> 
>     /** Default size threshold for Amazon S3 object after which multi-part copy is initiated. */
>     @SdkTestInternalApi
>     static final long DEFAULT_MULTIPART_COPY_THRESHOLD = 5 * GB;
> 
>     /** Default minimum size of each part for multi-part copy. */
>     @SdkTestInternalApi
>     static final long DEFAULT_MINIMUM_COPY_PART_SIZE = 100 * MB;
> 
> ```

> ```java
> public class TransferManagerUtils {
> 
>     /**
>      * Returns a new thread pool configured with the default settings.
>      *
>      * @return A new thread pool configured with the default settings.
>      */
>     public static ThreadPoolExecutor createDefaultExecutorService() {
>         ThreadFactory threadFactory = new ThreadFactory() {
>             private int threadCount = 1;
> 
>             public Thread newThread(Runnable r) {
>                 Thread thread = new Thread(r);
>                 thread.setName("s3-transfer-manager-worker-" + threadCount++);
>                 return thread;
>             }
>         };
>         return (ThreadPoolExecutor)Executors.newFixedThreadPool(10, threadFactory);
>     }
> ```

また、アップロードするファイルサイズが不明の場合であったり、マルチパートアップロードを細かく制御したい場合には
`AmazonS3#uploadPart` を使用してアップロードを行う。

* [AWS Java SDK を使用したマルチパートアップロード (低レベル API)](https://docs.aws.amazon.com/ja_jp/AmazonS3/latest/dev/mpListPartsJavaAPI.html)
* https://www.programcreek.com/java-api-examples/index.php?api=com.amazonaws.services.s3.model.InitiateMultipartUploadRequest
* https://www.programcreek.com/java-api-examples/index.php?api=com.amazonaws.services.s3.model.UploadPartRequest

// todo: あとで書く


## ダウンロード


