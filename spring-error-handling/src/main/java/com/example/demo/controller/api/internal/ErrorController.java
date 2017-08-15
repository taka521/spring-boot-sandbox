package com.example.demo.controller.api.internal;

import com.example.demo.controller.GlobalErrorController;
import com.example.demo.domain.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController("apiInternalErrorController")
public class ErrorController {

    @RequestMapping("/api/internal/error")
    public ErrorResponse error(final HttpServletRequest request) {

        final Throwable error = (Throwable) request.getAttribute(GlobalErrorController.ERROR_EXCEPTION);
        final String errorCode = "internalError";

        log.error(error.getMessage(), error);

        return ErrorResponse.of(errorCode, error);
    }
}
