package com.github.liyibo1110.mybatis.demo;

import com.github.liyibo1110.mybatis.annotation.Entity;
import com.github.liyibo1110.mybatis.annotation.Select;

@Entity(User.class)
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = ?")
    User getUserById(Integer uid);
}
