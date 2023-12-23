package org.example.entity.auth;

import java.time.LocalDateTime;

public class ElementOperationRoleAssociation {
    private Long id;
    private Long elementOperationId;
    private Long roleId;
    private LocalDateTime createdTime;
    private AuthElementOperation authElementOperation;

    public Long getId() {
        return id;
    }

    public Long getElementOperationId() {
        return elementOperationId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public AuthElementOperation getAuthElementOperation() {
        return authElementOperation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setElementOperationId(Long elementOperationId) {
        this.elementOperationId = elementOperationId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setAuthElementOperation(AuthElementOperation authElementOperation) {
        this.authElementOperation = authElementOperation;
    }
}
