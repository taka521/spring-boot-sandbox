package com.example.validation_demo;

import com.example.validation_demo.domain.Password;
import com.example.validation_demo.domain.UserId;
import com.example.validation_demo.validation.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class DemoForm {

    private UserId userId;

    @NotEmpty(message = "passwordは未入力でござる")
    private Password password;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;

}
