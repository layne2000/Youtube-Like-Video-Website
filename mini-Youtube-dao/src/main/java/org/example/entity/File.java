package org.example.entity;

import java.time.LocalDateTime;
import java.util.Date;

public class File {
    private Long id;
    private String url;
    private String type;
    private String SHA256;
    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getSHA256() {
        return SHA256;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSHA256(String SHA256) {
        this.SHA256 = SHA256;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
