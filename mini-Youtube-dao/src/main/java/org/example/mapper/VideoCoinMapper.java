package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.VideoCoin;

import java.time.LocalDateTime;

@Mapper
public interface VideoCoinMapper {

    @Select("SELECT * FROM t_video_coin WHERE videoId = #{videoId} AND userId = #{userId} FOR UPDATE")
    VideoCoin getVideoCoinByVideoIdAndUserId(Long videoId, Long userId);

    @Insert("INSERT INTO t_video_coin (userId, videoId, amount, createdTime) " +
            "VALUES(#{userId}, #{videoId}, #{amount}, #{createdTime})")
    void insertVideoCoin(VideoCoin videoCoin);

    @Update("UPDATE t_video_coin SET amount = #{amount}, updatedTime = #{updatedTime}" +
            "WHERE userId = #{userId} AND videoId = #{videoId}")
    void updateAmountAndTime(VideoCoin videoCoin);

    @Select("SELECT sum(amount) FROM t_video_coin WHERE videoId = #{videoId}")
    Long getVideoCoinTotalAmount(Long videoId);
}
