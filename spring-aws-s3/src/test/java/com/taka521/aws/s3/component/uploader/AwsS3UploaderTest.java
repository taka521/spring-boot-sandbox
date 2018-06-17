package com.taka521.aws.s3.component.uploader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;

/**
 * {@link AwsS3Uploader}のテストクラス
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwsS3UploaderTest {

    private static final Logger logger = LoggerFactory.getLogger(AwsS3UploaderTest.class);

    /** アップローダー */
    @Autowired
    private AwsS3Uploader uploader;

    @Test
    public void encryptUpload() throws Exception {
        final String filePath = this.getClass().getClassLoader().getResource("test.txt").getPath();
        final File inputFile = new File(filePath);
        logger.info("file name : {}", inputFile.getName());
        logger.info("file length : {}", inputFile.length());

        // リクエスト作成
        final AwsS3UploaderRequest request = AwsS3UploaderRequest.builder()
                .contentType("text/plain")
                .fileName("encryptUpload.txt")
                .inputStream(new FileInputStream(inputFile))
                .contentLength((int)inputFile.length())
                .build();

        // 実行
        final String result = this.uploader.encryptUpload(request, true);
    }
}