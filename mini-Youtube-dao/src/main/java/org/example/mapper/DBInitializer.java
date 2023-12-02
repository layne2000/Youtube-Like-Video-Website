package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DBInitializer {

    @Update("DROP TABLE IF EXISTS users;")
    void dropUsersTable();

    @Update("CREATE TABLE users ("
            + "user_id VARCHAR(255) PRIMARY KEY NOT NULL,"
            + "email VARCHAR(255) NOT NULL,"
            + "password VARCHAR(255) NOT NULL"
            + ");")
    void createUsersTable();
}
