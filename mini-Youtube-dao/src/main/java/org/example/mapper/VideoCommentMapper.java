package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.VideoComment;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoCommentMapper {

    @Insert("INSERT INTO t_video_comment (videoId, userId, replyUserId, rootCommentId, comment, " +
            "createdTime, updatedTime) VALUES(#{videoId}, #{userId}, #{replyUserId}, " +
            "#{rootCommentId}, #{comment}, #{createdTime}, #{updatedTime})")
    void insertVideoComment(VideoComment videoComment);

    @Select("SELECT * FROM t_video_comment WHERE videoId = #{videoId} AND rootId is NULL" +
            "ORDER BY id DESC LIMIT BY #{start}, #{limit}")
    List<VideoComment> pageListRootVideoComments(Map<String, Object> videoCommentParams);

    @Select("<script>" +
            "SELECT *" +
            "FROM t_video_comment WHERE rootCommentId IN " +
            "<foreach item='rootCommentId' collection='rootCommentIdList' open='(' separator=',' close=')'>" +
            "#{rootCommentId}" +
            "</foreach>" +
            "</script>")
    List<VideoComment> getVidoCommentListByRootCommentIdList(List<Long> rootCommentIdList);
}
