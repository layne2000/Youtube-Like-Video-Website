package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.UserMoment;

@Mapper
public interface UserMomentMapper {
    @Insert("INSERT INTO t_user_moment(userId, type, contentId, createdTime) VALUES" +
            "(#{userId}, #{type}, #{contentId}, #{createdTime})")
    void addUserMoment(UserMoment userMoment);
}
