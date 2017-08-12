package com.example.domain_form.dao;

import com.example.domain_form.domain.ID;
import com.example.domain_form.entity.User;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface UserDao {

    @Select(strategy = SelectType.COLLECT)
    <R> R findAll(final Collector<User, ?, R> collector);

    @Select
    Optional<User> findById(final ID<User> id);

}
