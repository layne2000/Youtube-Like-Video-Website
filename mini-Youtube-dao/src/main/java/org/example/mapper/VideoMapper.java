package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.Video;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoMapper {
    @Select("INSERT INTO t_video (userId, url, thumbnail, title, type, duration, section, description, createdTime, updatedTime) " +
            "VALUES(#{userId}, #{url}, #{thumbnail}, #{title}, #{type}, #{duration}, #{section}, #{description}, #{createdTime}, #{updatedTime})")
    void insertVideo(Video video);

    @Select("<script>" +
            "SELECT count(*) FROM t_video WHERE 1=1 " +
            "<if test=\"section != null and section != '' \"> AND section = #{section}</if>" +
            "</script>")
    Integer countVideosBySection(String section);

    @Select("<script>" +
            "SELECT * FROM t_video WHERE 1=1 " +
            "<if test=\"section != null and section != '' \"> AND section = #{section}</if>" +
            "ORDER BY id DESC LIMIT #{start}, #{limit}" +
            "</script>")
    List<Video> pageListVideos(Map<String, Object> mapperParams);

    @Select("SELECT * FROM t_video WHERE id = #{id}")
    Video getVideoById(Long id);
}
