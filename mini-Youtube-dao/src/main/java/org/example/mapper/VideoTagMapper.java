package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.VideoTag;

import java.util.List;

@Mapper
public interface VideoTagMapper {

    @InsertProvider(type = VideoTagListSqlProvider.class, method = "insertVideoTagListSql")
    void addVideoTagList(@Param("videoTagList")List<VideoTag> videoTagList);
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
