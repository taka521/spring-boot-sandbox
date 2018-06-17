package com.taka521.aws.s3.component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;
import com.taka521.aws.s3.config.AmazonS3Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class AwsS3AuthComponent {

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(AwsS3AuthComponent.class);

    /** S3設定情報 */
    private final AmazonS3Setting s3Setting;

    @Autowired
    public AwsS3AuthComponent(final AmazonS3Setting s3Setting) {
        this.s3Setting = s3Setting;
    }

    /**
     * 認証を行い、S3アクセスオブジェクトを返却します。
     *
     * @return S3アクセスオブジェクト
     */
    public AmazonS3 auth() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(this.s3Setting.getRegion())
                .withCredentials(this.credentials())
                .build();
    }

    /**
     * 認証を行い、暗号化用のS3アクセスオブジェクトを返却します。
     *
     * @return S3アクセスオブジェクト
     */
    public AmazonS3 authWithEncrypt() {
        // 設定情報から暗号化キーを取得し、EncryptionMaterialsを生成する。
        // なお、AWS SDKは256byteのキーを必要とする。
        final byte[] keyBytes = this.s3Setting.getEncryptKey().getBytes(StandardCharsets.UTF_8);
        final SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        final EncryptionMaterials materials = new EncryptionMaterials(secretKey);

        return AmazonS3EncryptionClientBuilder.standard()
                .withRegion(this.s3Setting.getRegion())
                .withCredentials(this.credentials())
                .withEncryptionMaterials(new StaticEncryptionMaterialsProvider(materials))
                .build();
    }

    /**
     * 資格情報を生成します。
     *
     * @return 資格情報
     */
    private AWSCredentialsProvider credentials() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(this.s3Setting.getAccessKey(), this.s3Setting.getSecretKey()));
    }
}
