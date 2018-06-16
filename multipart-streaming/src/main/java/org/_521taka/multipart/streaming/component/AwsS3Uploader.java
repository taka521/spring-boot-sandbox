package org._521taka.multipart.streaming.component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import org._521taka.multipart.streaming.config.AmazonS3Setting;
import org._521taka.multipart.streaming.constant.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;

/**
 * AWS S3アップロードコンポーネント
 */
@Component
public class AwsS3Uploader {

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(AwsS3Uploader.class);

    private static final long MINIMUMi_UPLOAD_PART_SIZE = 5L * 1024L * 1024L;

    /** S3設定情報 */
    private final AmazonS3Setting s3Setting;

    @Autowired
    public AwsS3Uploader(final AmazonS3Setting s3Setting) {
        this.s3Setting = s3Setting;
    }

    /**
     * １回のオペレーションでアップロードを行います。<br/>
     * ファイルサイズは
     * <ul>
     * <li>最大：5TB</li>
     * </ul>
     * 以下です。
     *
     * @param source      アップロード対象のデータストリーム
     * @param contentType コンテントタイプ
     *
     * @return アップロード結果オブジェクト
     */
    public PutObjectResult singleUpload(final InputStream source, final ContentType contentType,
                                        final int contentLength) {

        final PutObjectRequest request = this.createPutObjectRequest(source, contentType, contentLength);
        final AmazonS3 s3 = this.auth();

        try {
            return s3.putObject(request);
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage(), e);
            throw e;
        } catch (SdkClientException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * マルチパートアップロードを行います。<br/>
     * マルチパートアップロードを行う場合、
     * <ul>
     * <li>最低：5MB</li>
     * <li>最高：5TB</li>
     * </ul>
     * の範囲のファイルが対象です。
     *
     * @param multipartSource アップロード対象のデータストリーム
     * @param contentType     コンテントタイプ
     *
     * @return アップロード結果オブジェクト
     */
    public UploadResult multipartUpload(final InputStream multipartSource, final ContentType contentType,
                                        final int contentLength) {
        final PutObjectRequest request = this.createPutObjectRequest(multipartSource, contentType, contentLength);
        final AmazonS3 s3 = this.auth();
        final TransferManager manager = TransferManagerBuilder.standard()
                .withS3Client(s3)
                .build();

        try {
            final Upload upload = manager.upload(request);
            final UploadResult result = upload.waitForUploadResult();
            manager.shutdownNow();
            return result;
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage(), e);
            throw e;
        } catch (AmazonClientException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 認証を行い、S3クライアントを取得します。
     *
     * @return S3クライアント
     */
    private AmazonS3 auth() {
        final AWSCredentials credentials = new BasicAWSCredentials(this.s3Setting.getAccessKey(),
                                                                   this.s3Setting.getSecretKey());
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(this.s3Setting.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    /**
     * S3アップロードを行うために、{@link PutObjectRequest}を作成します。
     *
     * @param source      アップロード対象となるデータストリーム
     * @param contentType コンテントタイプ
     *
     * @return プットオブジェクト
     */
    private PutObjectRequest createPutObjectRequest(final InputStream source, final ContentType contentType,
                                                    final int contentLength) {

        // Content-type と タイトルを設定
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType.getValue());
        metadata.setContentLength(contentLength);

        // バケット名、アクセスキー、アップロードファイル、メタデータを元にPutObjectを生成
        // なお、総転送数をログに出力するリスナーを設定。
        final PutObjectRequest request = new PutObjectRequest(this.s3Setting.getBucketName(), now(), source, metadata);

        request.setGeneralProgressListener(
                progressEvent -> logger.debug("Transferred bytes: {}", progressEvent.getBytesTransferred()));

        return request;
    }

    /**
     * yyyyMMddHHmmss形式の現在日時文字列を取得します。
     *
     * @return yyyyMMddHHmmss形式の現在日時文字列
     */
    private static String now() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }
}
