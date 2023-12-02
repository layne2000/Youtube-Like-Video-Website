package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entity.User;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (user_id, email, password) VALUES (#{userId}, #{email}, #{password})")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
    })
    User getUserById(String userId);

    @Update("UPDATE users SET email = #{email}, password = #{password} WHERE user_id = #{userId}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    void deleteUserById(String userId);

}
