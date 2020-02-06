package com.neuai.orm.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.neuai.bean.entity.Users;

import java.util.List;

/**
 * Created by neuai.com
 */
@Component
@Mapper
public interface UserMapper {

    Users selectByUserId(String userId);

    List<Users> selectAllUser();

}
