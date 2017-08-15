package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class GlobalErrorController implements ErrorController {

    public static final String ERROR_EXCEPTION = "com.example.demo.error.exception";

    @Value("${server.error.path:{$error.path:/error}}")
    private String errorPath;

    private ErrorAttributes errorAttributes;

    public GlobalErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return this.errorPath;
    }

    /**
     * エラーの補足を一手に引き受けるためのHandleメソッド。<br/>
     * アプリケーションでエラーが発生した場合 application.properties に定義している、server.error.path の設定URLに飛ばされる。<br/>
     * そのエラーURLにマッピングする Handleメソッド を定義する。<br/>
     * このメソッドでは、エラー（Throwable）を取得して、Siteごとに定義したエラーコントローラへフォワードさせる。
     *
     * @param request Httpリクエスト
     *
     * @return Siteごとのフォワード先
     */
    @RequestMapping("${server.error.path:{$error.path:/error}}")
    public String error(final HttpServletRequest request) {

        Throwable error = errorAttributes.getError(new ServletRequestAttributes(request));
        String requestURI = (String) request.getAttribute("javax.servlet.forward.request_uri");
        Site site = Site.of(requestURI);

        request.setAttribute(ERROR_EXCEPTION, error);

        log.warn("Caught an error. site = {}, request = {}", site, requestURI);

        // Siteごとのエラーパスへフォワードさせる。
        return "forward:" + site.getBaseUrl() + "/error";
    }

}
