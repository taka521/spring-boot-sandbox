package com.example.demo.controller.screen.user;

import com.example.demo.controller.GlobalErrorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller("userErrorController")
public class ErrorController {

    @RequestMapping("/error")
    public String error(final HttpServletRequest request, final Model model) {

        final Throwable e = (Throwable) request.getAttribute(GlobalErrorController.ERROR_EXCEPTION);

        final String errorCode = "screenUserError";
        final String method = request.getMethod();

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("errorClass", e);
        model.addAttribute("method", method);

        log.error(e.getMessage(), e);

        return "screen/user/error";
    }
}
