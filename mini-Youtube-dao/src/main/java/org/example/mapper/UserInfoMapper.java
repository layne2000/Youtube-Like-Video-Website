package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.UserInfo;

@Mapper
public interface UserInfoMapper {
    @Insert("INSERT INTO t_user_info(userId, nickname, avatar, sign, gender, birthday, createdTime, " +
            "updatedTime) VALUES (#{userId}, #{nickname}, #{avatar}, #{sign}, #{gender}, #{birthday}, " +
            "#{createdTime}, #{updatedTime})")
    void insertUserInfo(UserInfo userInfo);

    @Select("SELECT * from t_user_info WHERE userId = #{userId}")
    UserInfo getUserInfoByUserId(Long userId);
}
