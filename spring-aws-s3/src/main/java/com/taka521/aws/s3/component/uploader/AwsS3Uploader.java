package com.taka521.aws.s3.component.uploader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.ExecutorFactory;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.taka521.aws.s3.component.AwsS3AuthComponent;
import com.taka521.aws.s3.config.AmazonS3Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * Amazon S3アップローダー
 */
@Component
public class AwsS3Uploader {

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(AwsS3Uploader.class);

    /** 認証コンポーネント */
    private final AwsS3AuthComponent authComponent;

    /** S3設定情報 */
    private final AmazonS3Setting s3Setting;

    @Autowired
    public AwsS3Uploader(final AwsS3AuthComponent authComponent, final AmazonS3Setting s3Setting) {
        this.authComponent = authComponent;
        this.s3Setting = s3Setting;
        logger.info("AwsS3AuthComponent : {}", this.authComponent);
        logger.info("AmazonS3Setting : {}", this.s3Setting);
    }

    /**
     * アップロード対象のデータを暗号化して、S3アップロードを行います。
     *
     * @param request リクエスト情報
     *
     * @return Eタグ
     */
    public String encryptUpload(final AwsS3UploaderRequest request, final boolean wantMultipartUpload) {
        logger.info("Request Param : {}", request.toString());

        final AmazonS3 s3 = this.authComponent.authWithEncrypt();
        final ObjectMetadata metadata = new ObjectMetadata();
        if (request.contentLength > 0) {
            metadata.setContentLength(request.contentLength);
        }

        final PutObjectRequest putObject = new PutObjectRequest(this.s3Setting.getBucketName(), request.fileName,
                                                                request.inputStream, metadata);
        putObject.setGeneralProgressListener(event -> logger.debug("transfer size : {}", event.getBytesTransferred()));

        if (wantMultipartUpload) {
            return this.multipartUpload(s3, putObject);
        } else {
            return s3.putObject(putObject).getETag();
        }
    }

    /**
     * マルチパートアップロードを行います。
     *
     * @param s3               S3クライアント
     * @param putObjectRequest アップロードオブジェクト
     *
     * @return Eタグ
     */
    private String multipartUpload(final AmazonS3 s3, final PutObjectRequest putObjectRequest) {
        // マルチパートアップロードを行う閾値。この値を超えるとマルチパートアップロードを行う。
        final long multipartUploadThreshold = 5L * 1024L * 1024L;

        // マルチパートアップロード時の分割サイズ。小さすぎると逆に遅くなってしまうので注意。
        final long minimumUploadPartSize = Math.max(putObjectRequest.getMetadata().getContentLength() / 4,
                                                    multipartUploadThreshold);

        // アップロード
        final TransferManager transferManager = TransferManagerBuilder.standard()
                .withS3Client(s3)
                .withMultipartUploadThreshold(multipartUploadThreshold)
                .withMinimumUploadPartSize(minimumUploadPartSize)
                .withExecutorFactory(() -> Executors.newFixedThreadPool(4))
                .build();
        final Upload upload = transferManager.upload(putObjectRequest);
        try {
            return upload.waitForUploadResult().getETag();
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage(), e);
            throw new RuntimeException(e);
        } catch (AmazonClientException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
