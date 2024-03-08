package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entity.VideoTag;

import java.util.List;

@Mapper
public interface VideoTagMapper {

    @InsertProvider(type = VideoTagListSqlProvider.class, method = "insertVideoTagListSql")
    void addVideoTagList(@Param("videoTagList")List<VideoTag> videoTagList);

    @Select("SELECT * FROM t_video_tag WHERE videoId = #{videoId}")
    List<VideoTag> getVideoTagListByVideoId(Long videoId);

    @Select("<script>" +
            "SELECT *" +
            "FROM t_video_tag WHERE videoId = #{videoId} AND tagId IN " +
            "<foreach item='tagId' collection='tagIdList' open='(' separator=',' close=')'>" +
            "#{tagId}" +
            "</foreach>" +
            "</script>")
    void deleteVideoTagsByTagIdList(List<Long> tagIdList, Long videoId);
}

class VideoTagListSqlProvider {

    public String insertVideoTagListSql(@Param("videoTagList") List<VideoTag> videoTagList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO t_video_tag (videoId, tagId, createdTime) VALUES ");
        for (int i = 0; i < videoTagList.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("(#{videoTagList[").append(i).append("].videoId}, #{videoTagList[").append(i).
                    append("].tagId}, #{videoTagList[").append(i).append("].createdTime})");
        }
        return sql.toString();
    }
}
