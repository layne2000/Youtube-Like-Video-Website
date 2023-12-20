package org.example.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import org.example.entity.User;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO t_user (phone, email, password, salt, createdTime, updatedTime) " +
            "VALUES (#{phone}, #{email}, #{password}, #{salt}, #{createdTime}, #{updatedTime})")
    void insertUser(User user);

    @Select("SELECT * from t_user where phone = #{phone}")
    User getUserByPhone(String phone);

    @Select("SELECT * from t_user where email = #{email}")
    User getUserByEmail(String email);

    @Select("SELECT * from t_user where phone = #{phone} or email = #{email}")
    User getUserByPhoneOrEmail(String phone, String email);

    @Select("SELECT * from t_user where id = #{id}")
    User getUserById(Long id);

    @Update("<script>" +
            "UPDATE t_user" +
            "<set>" +
            "  <if test=\"phone != null and phone != '' \"> phone = #{phone},</if>" +
            "  <if test=\"email != null and email != '' \"> email = #{email},</if>" +
            "  <if test=\"password != null and password != '' \"> password = #{password},</if>" +
            "  updatedTime = #{updatedTime}" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    void updateUser(User user);

}
