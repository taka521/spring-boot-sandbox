package com.taka521.aws.s3.component.uploader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@link AwsS3Uploader}のテストクラス
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwsS3UploaderTest {

    /** アップローダー */
    @Autowired
    private AwsS3Uploader uploader;

    @Test
    public void encryptUpload() throws Exception {
        // リクエスト作成
        final AwsS3UploaderRequest request = AwsS3UploaderRequest.builder()
                .contentType("text/plain")
                .fileName("encryptUpload.txt")
                .inputStream(this.getClass().getClassLoader().getResource("test.txt").openStream())
                .build();

        // 実行
        final String result = this.uploader.encryptUpload(request);
    }
}