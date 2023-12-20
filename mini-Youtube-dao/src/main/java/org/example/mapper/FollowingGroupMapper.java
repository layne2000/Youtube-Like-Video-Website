package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.FollowingGroup;

import java.util.List;

@Mapper
public interface FollowingGroupMapper {
    @Select("SELECT * FROM t_following_group WHERE id = #{id}")
    FollowingGroup getFollowingGroupById(Long id);

    @Select("SELECT * FROM t_following_group WHERE type = #{type} AND userId = #{userId}")
    FollowingGroup getFollowingGroupByTypeAndUseId(String type, Long userId);

    @Select("SELECT * FROM t_following_group WHERE userId = #{userId}")
    List<FollowingGroup> getFollowingGroupListByUserId(Long userId);

    @Insert("INSERT INTO t_following_group (userId, name, type, createdTime) " +
            "VALUES(#{userId}, #{name}, #{type}, #{createdTime})")
    void addFollowingGroup(FollowingGroup followingGroup);
}
