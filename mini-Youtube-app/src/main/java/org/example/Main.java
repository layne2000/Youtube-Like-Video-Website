package org.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.DBInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.exit;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //traditional mybatis approach to initialize DB
        String resource = "mybatis-config.xml";
        InputStream inputStream;
        SqlSessionFactory sqlSessionFactory;

        try {
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing SqlSessionFactory", e);
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DBInitializer databaseInitializer = sqlSession.getMapper(DBInitializer.class);
            databaseInitializer.dropUserTable();
            databaseInitializer.dropUserInfoTable();
            databaseInitializer.createUserTable();
            databaseInitializer.createUserInfoTable();
            sqlSession.commit();
            //sqlSession.close(); //sqlSession will be closed automatically after leaving try block
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Can't connect to the database");
            exit(1);
        }
        SpringApplication.run(Main.class, args);
    }

}