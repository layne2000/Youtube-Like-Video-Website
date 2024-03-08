package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entity.VideoTagAssociation;

import java.util.List;

@Mapper
public interface VideoTagMapper {

    @InsertProvider(type = VideoTagListSqlProvider.class, method = "insertVideoTagListSql")
    void addVideoTagList(@Param("videoTagAssociationList")List<VideoTagAssociation> videoTagAssociationList);

    @Select("SELECT * FROM t_video_tag_association WHERE videoId = #{videoId}")
    List<VideoTagAssociation> getVideoTagListByVideoId(Long videoId);

    @Delete("<script>" +
            "DELETE FROM t_video_tag_association WHERE videoId = #{videoId} AND tagId IN " +
            "<foreach item='tagId' collection='tagIdList' open='(' separator=',' close=')'>" +
            "#{tagId}" +
            "</foreach>" +
            "</script>")
    void deleteVideoTagsByTagIdList(List<Long> tagIdList, Long videoId);

    class VideoTagListSqlProvider {

        public String insertVideoTagListSql(@Param("videoTagAssociationList") List<VideoTagAssociation> videoTagAssociationList) {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO t_video_tag_association (videoId, tagId, createdTime) VALUES ");
            for (int i = 0; i < videoTagAssociationList.size(); i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("(#{videoTagAssociationList[").append(i).append("].videoId}, #{videoTagAssociationList[").append(i).
                        append("].tagId}, #{videoTagAssociationList[").append(i).append("].createdTime})");
            }
            return sql.toString();
        }
    }
}

