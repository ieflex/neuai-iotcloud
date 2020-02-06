package com.neuai.orm.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neuai.bean.entity.Users;

/**
 * Created by neuai.com
 */
public interface UserRepository extends JpaRepository<Users,String> {
}
