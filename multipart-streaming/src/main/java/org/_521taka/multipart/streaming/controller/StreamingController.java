package org._521taka.multipart.streaming.controller;

import org._521taka.multipart.streaming.filter.SizeCheckInputStreamFilter;
import org._521taka.multipart.streaming.service.StreamingService;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.FilterInputStream;

/**
 * ストリーミングコントローラ
 */
@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StreamingController {

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(StreamingController.class);

    /** ストリーミング上限サイズ */
    private static final long STREAMING_LIMIT_SIZE = 1024L * 1024L;

    /** ストリーミングサービス */
    private StreamingService streamingService;

    @Autowired
    public StreamingController(final StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @PostMapping("/streaming")
    public String streaming(final HttpServletRequest request) throws Exception {

        // マルチパートのリクエストであるか
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("request is not multipart.");
        }

        // マルチパートのアイテムを取得し、シーケンシャルに処理する
        final ServletFileUpload servletFileUpload = new ServletFileUpload();
        final FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);

        do {
            final FileItemStream itemStream = fileItemIterator.next();

            logger.info("field name : {}", itemStream.getFieldName());
            logger.info("file name : {}", itemStream.getName());
            logger.info("content type : {}", itemStream.getContentType());

            // ストリーミングサイズの上限をチェックするためのフィルター
            final FilterInputStream filterInputStream = new SizeCheckInputStreamFilter(itemStream.openStream(),
                                                                                       STREAMING_LIMIT_SIZE);
            this.streamingService.execute(filterInputStream, itemStream.getName());
        } while (fileItemIterator.hasNext());

        return "OK";
    }

}
