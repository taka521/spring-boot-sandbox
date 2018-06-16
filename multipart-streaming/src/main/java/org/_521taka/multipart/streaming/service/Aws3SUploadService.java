package org._521taka.multipart.streaming.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import org._521taka.multipart.streaming.component.AwsS3Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String execute(final Aws3SUploadServiceRequest request) {

        // todo: イケてないのであとでリファクタリング
        if (request.getContentLength() >= MULTIPART_UPLOAD_MIN_SIZE) {
            logger.info("select multipart upload.");
            final UploadResult result = this.uploader.multipartUpload(request.getSource(), request.getContentType(),
                                                                      request.getContentLength());
            return result.getETag();
        } else {
            logger.info("select single upload.");
            final PutObjectResult result = this.uploader.singleUpload(request.getSource(), request.getContentType(),
                                                                      request.getContentLength());
            return result.getETag();
        }
    }

}
