package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.File;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO t_file(url, type, SHA256, createdTime) VALUES(#{url}, #{type}, #{SHA256}, #{createdTime})")
    void addFile(File file);

    @Select("SELECT * FROM t_file WHERE SHA256 = #{SHA256}")
    File getFileBySHA256(String SHA256);
}
