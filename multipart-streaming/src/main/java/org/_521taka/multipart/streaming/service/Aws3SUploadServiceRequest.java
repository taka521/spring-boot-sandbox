package org._521taka.multipart.streaming.service;

import org._521taka.multipart.streaming.constant.ContentType;

import java.io.InputStream;

public class Aws3SUploadServiceRequest {

    private final InputStream source;
    private final ContentType contentType;
    private final int contentLength;

    public Aws3SUploadServiceRequest(final InputStream source, final ContentType contentType, final int contentLength) {
        this.source = source;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public InputStream getSource() {
        return source;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }
}
