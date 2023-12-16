package org.example.entity;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String phone;
    private String email;
    private String password;
    private String salt;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String phone, String email, String password, String salt, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
