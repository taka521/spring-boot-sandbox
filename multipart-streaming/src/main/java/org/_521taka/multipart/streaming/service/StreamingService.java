package org._521taka.multipart.streaming.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedInputStream;
import java.io.InputStream;

@Service
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StreamingService {

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(StreamingService.class);

    public void execute(final InputStream in, final String fileName) {
        try(final BufferedInputStream bis = new BufferedInputStream(in)) {

            int len;
            byte[] buff = new byte[1024];
            while((len = bis.read(buff)) != -1) {
                logger.info(new String(buff));
            }

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
