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
            databaseInitializer.createUserTable();
            databaseInitializer.dropUserInfoTable();
            databaseInitializer.createUserInfoTable();
            databaseInitializer.dropFollowingGroupTable();
            databaseInitializer.createFollowingGroupTable();
            databaseInitializer.dropUserFollowingTable();
            databaseInitializer.createUserFollowingTable();
            databaseInitializer.dropUserMomentTable();
            databaseInitializer.createUserMomentTable();
            databaseInitializer.dropAuthRoleTable();
            databaseInitializer.createAuthRoleTable();
            databaseInitializer.dropUserRoleAssociationTable();
            databaseInitializer.createUserRoleAssociationTable();
            databaseInitializer.dropAuthElementOperationTable();
            databaseInitializer.createAuthElementOperationTable();
            databaseInitializer.dropElementOperationRoleAssociationTable();
            databaseInitializer.createElementOperationRoleAssociationTable();
            databaseInitializer.dropAuthMenuTable();
            databaseInitializer.createAuthMenuTable();
            databaseInitializer.dropMenuRoleAssociationTable();
            databaseInitializer.createMenuRoleAssociationTable();
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