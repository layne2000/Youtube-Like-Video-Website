package org.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.UserFollowing;

import java.util.List;

@Mapper
public interface UserFollowingMapper {

    @Insert("INSERT INTO t_user_following (userId, followingId, groupId, createdTime) " +
            "VALUES(#{userId}, #{followingId}, #{groupId}, #{createdTime})")
    void addUserFollowing(UserFollowing userFollowing);

    @Delete("DELETE FROM t_user_following WHERE userId = #{userId} AND followingId = #{followingId}")
    void deleteUserFollowing(Long userId, Long followingId);

    @Select("SELECT * FROM t_user_following WHERE userId = #{userId}")
    List<UserFollowing> getUserFollowingList(Long userId);

    @Select("SELECT * FROM t_user_following WHERE followingId = #{userId}")
    List<UserFollowing> getUserFans(Long userId);

    @Select("SELECT * FROM t_user_following WHERE userId = #{userId} AND followingId = #{followingId}")
    UserFollowing getUserFollowingByUserIdAndFollowingId(Long userId, Long followingId);
}
