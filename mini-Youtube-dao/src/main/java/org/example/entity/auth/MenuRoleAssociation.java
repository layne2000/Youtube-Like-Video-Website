package org.example.entity.auth;

import java.time.LocalDateTime;

public class MenuRoleAssociation {
    private Long id;
    private Long menuId;
    private Long roleId;
    private LocalDateTime createdTime;
    private AuthMenu authMenu;

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public AuthMenu getAuthMenu() {
        return authMenu;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setAuthMenu(AuthMenu authMenu) {
        this.authMenu = authMenu;
    }
}
