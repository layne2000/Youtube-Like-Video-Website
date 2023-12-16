package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entity.User;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO t_user (phone, email, password, salt, createdTime, updatedTime) " +
            "VALUES (#{phone}, #{email}, #{password}, #{salt}, #{createdTime}, #{updatedTime})")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
    })
    User getUserById(String userId);

    @Select("Select * from t_user where phone = #{phone}")
    User getUserByPhone(String phone);

    @Update("UPDATE users SET email = #{email}, password = #{password} WHERE user_id = #{userId}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    void deleteUserById(String userId);

}
