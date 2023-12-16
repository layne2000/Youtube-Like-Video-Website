package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.UserInfo;

@Mapper
public interface UserInfoMapper {
    @Insert("INSERT INTO t_user_info(userId, nickname, avatar, sign, gender, birthday, createdTime, " +
            "updatedTime) VALUES(#(userId), #(nickname), #(avatar), #(sign), #(gender), #(createdTime)," +
            " #(updatedTime))")
    void insertUserInfo(UserInfo userInfo);
}
