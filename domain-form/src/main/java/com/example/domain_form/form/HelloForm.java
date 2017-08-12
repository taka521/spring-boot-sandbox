package com.example.domain_form.form;

import com.example.domain_form.domain.ID;
import com.example.domain_form.domain.Name;
import com.example.domain_form.entity.User;

import java.io.Serializable;

public class HelloForm implements Serializable {

    private static final long serialVersionUID = 1L;

    public ID<User> userId;
    public Name<User> name;

    public ID<User> getUserId() {
        return userId;
    }

    public void setUserId(ID<User> userId) {
        this.userId = userId;
    }

    public Name<User> getName() {
        return name;
    }

    public void setName(Name<User> name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HelloForm{" + "userId=" + userId + ", name=" + name + '}';
    }
}
