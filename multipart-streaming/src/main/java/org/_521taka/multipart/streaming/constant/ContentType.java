package org._521taka.multipart.streaming.constant;

public enum  ContentType {
    TEXT("text/plain"),
    CSV("text/csv"),
    EXE("application/octet-stream"),
    PDF("application/pdf"),
    ZIP("application/zip"),
    GZIP("application/gzip"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif");

    private final String value;

    ContentType(final String value) {
        this.value = value;
    }

    public static ContentType of(final String value) {
        for(ContentType contentType : values()) {
            if (contentType.getValue().equalsIgnoreCase(value)) return contentType;
        }
        throw new IllegalArgumentException(value + "is don't match.");
    }

    public String getValue() {
        return value;
    }
}
