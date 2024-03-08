package org.example.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.UserInfo;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserInfoMapper {
    @Insert("INSERT INTO t_user_info(userId, nickname, avatar, sign, gender, birthday, createdTime, " +
            "updatedTime) VALUES (#{userId}, #{nickname}, #{avatar}, #{sign}, #{gender}, #{birthday}, " +
            "#{createdTime}, #{updatedTime})")
    void insertUserInfo(UserInfo userInfo);

    @Select("SELECT * from t_user_info WHERE userId = #{userId}")
    UserInfo getUserInfoByUserId(Long userId);

    @Update("<script> " +
            "UPDATE t_user_info " +
            "<set>" +
            "<if test = \"nickname != null and nickname != ''\"> nickname = #{nickname}, </if>"+
            "<if test = \"avatar !=null and avatar != ''\"> avatar = #{avatar}, </if>"+
            "<if test = \"sign !=null and sign != ''\">sign = #{sign}, </if>"+
            "<if test = \"gender != null and gender != ''\">gender = #{gender}, </if>"+
            "<if test = \"birthday != null and birthday != ''\">birthday = #{birthday}, </if>"+
            "updatedTime = #{updatedTime}"+
            "</set> " +
            "WHERE userId = #{userId}"+
            "</script>")
    void updateUserInfo(UserInfo userInfo);

    @Select("<script>" +
            "SELECT * FROM t_user_info WHERE 1=1 " +
            "<if test=\"nickname != null and nickname != ''\">" +
            "AND nickname LIKE '%${nickname}%'" +
            "</if>" +
            "ORDER BY id DESC " +
            "LIMIT #{start}, #{pageSize} " +
             "</script>")
    List<UserInfo> pageListUserInfo(JSONObject params);

    @Select("<script>" +
            "SELECT *" +
            "FROM t_user_info WHERE userId IN " +
            "<foreach item='userId' collection='userIdSet' open='(' separator=',' close=')'>" +
            "#{userId}" +
            "</foreach>" +
            "</script>")
    List<UserInfo> getUserInfoListByUserIdSet(Set<Long> userIdSet);
}
