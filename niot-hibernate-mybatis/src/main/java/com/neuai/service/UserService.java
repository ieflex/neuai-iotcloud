package com.neuai.service;

import java.util.List;

import com.neuai.bean.entity.Users;

/**
 * Created by neuai.com
 */
public interface UserService {

    Users save(Users user);

    Users findById(String userId);

    List<Users> findAll();
}
