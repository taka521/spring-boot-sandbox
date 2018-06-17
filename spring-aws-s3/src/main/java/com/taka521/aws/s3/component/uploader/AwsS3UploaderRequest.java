package com.taka521.aws.s3.component.uploader;

import java.io.InputStream;
import java.util.Objects;

public class AwsS3UploaderRequest {

    /** アップロード対象のストリーム */
    final InputStream inputStream;

    /** コンテンツ長 */
    final int contentLength;

    /** コンテントタイプ */
    final String contentType;

    /** ファイル名 */
    final String fileName;

    private AwsS3UploaderRequest(final AwsS3UploaderRequest.Builder builder) {
        this.inputStream = builder.inputStream;
        this.contentLength = builder.contentLength;
        this.contentType = builder.contentType;
        this.fileName = builder.fileName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InputStream inputStream;
        private int contentLength;
        private String contentType;
        private String fileName;

        public AwsS3UploaderRequest build() {
            Objects.requireNonNull(this.inputStream);
            Objects.requireNonNull(this.contentType);
            Objects.requireNonNull(this.fileName);

            return new AwsS3UploaderRequest(this);
        }

        public Builder inputStream(final InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public Builder contentLength(final int contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public Builder contentType(final String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder fileName(final String fileName) {
            this.fileName = fileName;
            return this;
        }
    }

    @Override
    public String toString() {
        return "AwsS3UploaderRequest{" + "inputStream=" + inputStream + ", contentLength=" + contentLength + ", contentType='" + contentType + '\'' + ", fileName='" + fileName + '\'' + '}';
    }
}
