package com.example.domain_form.entity;

import com.example.domain_form.domain.ID;
import com.example.domain_form.domain.Name;
import org.seasar.doma.Entity;

@Entity(immutable = true)
public class User {

    public final ID<User> id;
    public final Name<User> name;


    public User(final ID<User> id, final Name<User> name) {
        this.id = id;
        this.name = name;
    }
}
