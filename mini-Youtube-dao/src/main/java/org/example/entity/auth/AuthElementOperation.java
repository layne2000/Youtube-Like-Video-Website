package org.example.entity.auth;

import java.time.LocalDateTime;

public class AuthElementOperation {
    private Long id;
    private String elementName;
    private String elementCode;
    private String operationType;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() {
        return id;
    }

    public String getElementName() {
        return elementName;
    }

    public String getElementCode() {
        return elementCode;
    }

    public String getOperationType() {
        return operationType;
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

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
