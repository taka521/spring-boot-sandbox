package org._521taka.multipart.streaming.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws.s3")
public class AmazonS3Setting {

    private static final Logger logger = LoggerFactory.getLogger(AmazonS3Setting.class);

    /** region */
    private String region;

    /** アクセスキー */
    private String accessKey;

    /** シークレットキー */
    private String secretKey;

    /** バケット名 */
    private String bucketName;

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
        logger.info("region : {}", this.region);
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
        logger.info("accessKey : {}", this.accessKey);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
        logger.info("secretKey : {}", this.secretKey);
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
        logger.info("bucketName : {}", this.bucketName);
    }
}
