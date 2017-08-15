package com.example.demo.controller.screen.admin;

import com.example.demo.controller.GlobalErrorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller("adminErrorController")
public class ErrorController {

    @RequestMapping("/admin/error")
    public String error(final HttpServletRequest request, final Model model) {

        final Throwable e = (Throwable) request.getAttribute(GlobalErrorController.ERROR_EXCEPTION);
        final String errorCode = "screenAdminError";
        final String method = request.getMethod();

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("errorClass", e);
        model.addAttribute("method", method);

        log.error(e.getMessage(), e);

        return "screen/admin/error";
    }
}
