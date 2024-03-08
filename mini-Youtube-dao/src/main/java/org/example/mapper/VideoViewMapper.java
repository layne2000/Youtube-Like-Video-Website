package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.example.entity.VideoView;

import java.util.Map;

@Mapper
public interface VideoViewMapper {

    @Select("SELECT count(*) FROM t_video_view WHERE videoId = #{videoId}")
    Integer getCountByVideoId(Long videoId);

    @Insert("INSERT INTO t_video_view (videoId, userId, clientId, ip, createdTime) " +
            "VALUES(#{videoId}, #{userId}, #{clientId}, #{ip}, #{createdTime})")
    void insertVideoView(VideoView videoView);

    @SelectProvider(type = VideoViewSqlProvider.class, method = "getVideoView")
    VideoView getVideoView(Map<String, Object> params);

    class VideoViewSqlProvider{
        public String getVideoView(Map<String, Object> params){
            return new SQL() {{
                SELECT("*");
                FROM("t_video_view");
                WHERE("videoId = #{videoId} AND DATE(createdTime) = #{today}");
                if (params.get("userId") != null) {
                    WHERE("userId = #{userId}");
                }else{
                    if (params.get("clientId") != null && !params.get("clientId").equals("")) {
                        WHERE("clientId = #{clientId}");
                    }
                    if (params.get("ip") != null && !params.get("ip").equals("")) {
                        WHERE("ip = #{ip}");
                    }
                }
            }}.toString() + " FOR UPDATE";
        }
    }
}


