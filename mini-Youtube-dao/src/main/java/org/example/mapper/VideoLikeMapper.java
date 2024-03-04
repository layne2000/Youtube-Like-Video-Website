package org.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.VideoLike;

@Mapper
public interface VideoLikeMapper {
    @Select("SELECT * FROM t_video_like WHERE userId = #{userId} AND videoId = #{videoId} FOR UPDATE")
    VideoLike getVideoLikeByUserIdAndVideoId(Long userId, Long videoId);

    @Insert("INSERT INTO t_video_like (userId, videoId, createdTime) " +
            "VALUES(#{userid}, #{videoId}, #{createdTime})" )
    void insertVideoLike(VideoLike videoLike);

    @Delete("DELETE FROM t_video_like WHERE userId = #{userId} AND videoId = #{videoId}")
    void deleteVideoLike(Long userId, Long videoId);

    @Select("SELECT count(*) FROM t_video_like WHERE videoId = #{videoId}")
    Long getVideoLikeNum(Long videoId);
}
