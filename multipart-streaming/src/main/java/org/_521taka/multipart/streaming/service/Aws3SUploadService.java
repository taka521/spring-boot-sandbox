package org._521taka.multipart.streaming.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import org._521taka.multipart.streaming.component.AwsS3Uploader;
import org._521taka.multipart.streaming.constant.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * AWS S3アップロードサービス
 */
@Service
public class Aws3SUploadService {

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(Aws3SUploadService.class);

    private static final int MULTIPART_UPLOAD_MIN_SIZE = 5 * 1024 * 1024;

    /** アップローダー */
    private final AwsS3Uploader uploader;

    @Autowired
    public Aws3SUploadService(final AwsS3Uploader uploader) {
        this.uploader = uploader;
    }

    /**
     * S3へファイルをアップロードします。<br/>
     * ファイルサイズが不明の場合は0を指定してください。
     *
     * @param source      アップロード対象の入力ストリーム
     * @param contentType コンテントタイプ
     * @param fileSize    ファイルサイズ
     *
     * @return Eタグ
     */
    public String execute(final InputStream source, final String contentType, final int fileSize) {

        if (fileSize >= MULTIPART_UPLOAD_MIN_SIZE) {
            final UploadResult result = this.uploader.multipartUpload(source, ContentType.of(contentType));
            return result.getETag();
        } else {
            final PutObjectResult result = this.uploader.singleUpload(source, ContentType.of(contentType));
            return result.getETag();
        }
    }
}
