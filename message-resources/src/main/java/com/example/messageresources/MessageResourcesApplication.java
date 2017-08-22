package com.example.messageresources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Controller
@SpringBootApplication
public class MessageResourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageResourcesApplication.class, args);
    }

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/")
    public String hello(final Model model, final Locale locale) {

        // メッセージIDがシンプルな場合
        model.addAttribute("msg001", messageSource.getMessage("MSG0001", null, locale));

        // メッセージIDが「.」を含む場合
        model.addAttribute("msg002", messageSource.getMessage("INFO.MSG0002", null, locale));

        // メッセージに埋め込み文字が必要な場合
        model.addAttribute("msg003", messageSource.getMessage("INFO.MSG0003", new String[]{"田中"}, locale));

        // テンプレートに変数を渡して、ビュー側でメッセージを埋め込む場合。
        model.addAttribute("username", "ルフィ");

        return "hello";
    }


}
