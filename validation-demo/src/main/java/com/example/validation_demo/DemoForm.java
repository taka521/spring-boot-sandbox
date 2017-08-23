package com.example.validation_demo;

import com.example.validation_demo.domain.Password;
import com.example.validation_demo.domain.UserId;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@NoArgsConstructor
public class DemoForm {


    @NotNull
    private UserId userId;

    @Valid
    private Password password;

    @Past
    private Date date;

}
