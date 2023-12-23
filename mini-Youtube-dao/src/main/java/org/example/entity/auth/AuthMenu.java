package org.example.entity.auth;

import java.time.LocalDateTime;

public class AuthMenu {
    private Long id;
    private String menuName;
    private String menuCode;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
