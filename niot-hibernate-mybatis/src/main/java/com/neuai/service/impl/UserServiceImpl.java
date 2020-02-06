package com.neuai.service.impl;

import com.neuai.bean.entity.Users;
import com.neuai.orm.jpa.UserRepository;
import com.neuai.orm.mybatis.UserMapper;
import com.neuai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by neuai.com
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserRepository userRepository,UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public Users save(Users user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Users findById(String userId) {
        return userMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Users> findAll() {
        return userMapper.selectAllUser();
    }
}
