package org.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.VideoCollection;

@Mapper
public interface VideoCollectionMapper {

    @Delete("DELETE FROM t_video_collection WHERE videoId = #{videoId} AND userId = #{userId}")
    void deleteVideoCollectionByVideoIdAndUserId(Long videoId, Long userId);

    @Insert("INSERT INTO t_video_collection(videoId, userId, groupId, createdTime)" +
            "VALUES(#{videoId}, #{userId}, #{groupId}, #{createdTime})")
    void insertVideoCollection(VideoCollection videoCollection);

    @Select("SELECT count(*) FROM t_video_collection WHERE videoId = #{videoId}")
    Long getVideoCollectionNum(Long videoId);

    @Select("SELECT * FROM t_video_collection WHERE userId = #{userId} AND videoId = #{videoId}")
    VideoCollection getVideoCollectionByUserIdAndVideoId(Long userId, Long videoId);
}
