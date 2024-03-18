package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserCoinMapper {

    @Select("SELECT amount FROM t_user_coin WHERE userId = #{userId}")
    Long getAmountByUserId(Long userId);

    @Update("UPDATE t_user_coin SET amount = #{amount} WHERE userId = #{userId}")
    void updateAmountByUserId(Long userId, long amount);
}
