package org._521taka.multipart.streaming.controller;

import org._521taka.multipart.streaming.constant.ContentType;
import org._521taka.multipart.streaming.filter.SizeCheckInputStreamFilter;
import org._521taka.multipart.streaming.service.Aws3SUploadService;
import org._521taka.multipart.streaming.service.Aws3SUploadServiceRequest;
import org._521taka.multipart.streaming.service.StreamingService;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ストリーミングコントローラ
 */
@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StreamingController {

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(StreamingController.class);

    /** ストリーミング上限サイズ */
    private static final long STREAMING_LIMIT_SIZE = 1024L * 1024L * 1024L * 1024L;

    /** 　アップロードサービス */
    private Aws3SUploadService uploadService;

    @Autowired
    public StreamingController(final Aws3SUploadService uploadService) {
        this.uploadService = uploadService;
    }

    /**
     * マルチパートリクエストをストリーミングするコントローラ。
     *
     * @param fileSize ファイルサイズ
     * @param request  リクエストコンテキスト
     *
     * @return キーがファイル名、バリューがEタグのMap
     *
     * @throws Exception 例外
     */
    @PostMapping("/streaming")
    public Map<String, String> streaming(
            @RequestHeader(name = "X-FILE-SIZE", required = false, defaultValue = "0") final int fileSize,
            final HttpServletRequest request) throws Exception {

        // マルチパートのリクエストであるか
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("request is not multipart.");
        }

        // マルチパートのアイテムを取得し、シーケンシャルに処理する
        final ServletFileUpload servletFileUpload = new ServletFileUpload();
        final FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);

        final Map<String, String> uploadResult = new HashMap<>();
        do {
            final FileItemStream itemStream = fileItemIterator.next();

            logger.info("field name : {}", itemStream.getFieldName());
            logger.info("file name : {}", itemStream.getName());
            logger.info("content type : {}", itemStream.getContentType());
            if (!itemStream.getFieldName().equals("file")) continue;

            // ストリーミングサイズの上限をチェックするためのフィルター
            final FilterInputStream filterInputStream = new SizeCheckInputStreamFilter(itemStream.openStream(),
                                                                                       STREAMING_LIMIT_SIZE);
            // AWS 3Sへアップロードする
            final String eTag = this.uploadService.execute(
                    new Aws3SUploadServiceRequest(filterInputStream, ContentType.of(itemStream.getContentType()),
                                                  fileSize));
            uploadResult.put(itemStream.getName(), eTag);
        } while (fileItemIterator.hasNext());

        return uploadResult;
    }

}
