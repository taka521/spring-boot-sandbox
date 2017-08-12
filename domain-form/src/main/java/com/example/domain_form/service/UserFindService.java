package com.example.domain_form.service;

import com.example.domain_form.dao.UserDao;
import com.example.domain_form.domain.ID;
import com.example.domain_form.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFindService {

    /** ユーザDao */
    private final UserDao dao;

    public UserFindService(UserDao dao) {
        this.dao = dao;
    }

    public List<User> findAll() {
        return dao.findAll(Collectors.toList());
    }

    public Optional<User> findById(final ID<User> id) {
        return dao.findById(id);
    }

}
