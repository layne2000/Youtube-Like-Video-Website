package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.example.entity.LiveComment;

import java.util.List;
import java.util.Map;

@Mapper
public interface LiveCommentMapper {

    @Insert("INSERT INTO t_live_comment (userId, videoId, content, appearingTime, createdTime) " +
            "VALUES(#{userId}, #{videoId}, #{content}, #{appearingTime}, #{createdTime})")
    void insertLiveComment(LiveComment liveComment);

    @SelectProvider(type = LiveCommentSqlProvider.class, method = "getLiveCommentList")
    List<LiveComment> getLiveCommentList(Map<String, Object> liveCommentParams);

    class LiveCommentSqlProvider {

        public String getLiveCommentList(Map<String, Object> params) {
            return new SQL() {{
                SELECT("*");
                FROM("t_live_comment");
                WHERE("videoId = #{videoId}");
                if (params.get("startTime") != null && !params.get("startTime").equals("")) {
                    WHERE("appearingTime >= #{startTime}");
                }
                if (params.get("endTime") != null && !params.get("endTime").equals("")) {
                    WHERE("appearingTime <= #{endTime}");
                }
            }}.toString();
        }
    }
}
