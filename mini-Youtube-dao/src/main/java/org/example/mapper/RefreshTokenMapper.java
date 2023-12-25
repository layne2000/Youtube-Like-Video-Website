package org.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.RefreshToken;

import java.time.LocalDateTime;

@Mapper
public interface RefreshTokenMapper {
    @Delete("DELETE FROM t_refresh_token WHERE userId = #{userId}")
    void deleteRefreshTokenByUserId(Long userId);

    @Insert("INSERT INTO t_refresh_token(userId, refreshToken, createdTime) VALUES" +
            "(#{userId}, #{refreshToken}, #{createdTime})")
    void addRefreshToken(Long userId, String refreshToken, LocalDateTime createdTime);

    @Delete("DELETE FROM t_refresh_token WHERE userId = #{userId} AND refreshToken = #{refreshToken}")
    void deleteRefreshTokenByTokenAndUserId(Long userId, String refreshToken);

    @Select("SELECT * FROM t_refresh_token WHERE refreshToken = #{refreshTokenStr}")
    RefreshToken getByRefreshTokenStr(String refreshTokenStr);
}
