package org.example.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Video {
    private Long id;

    private Long userId;

    private String url;

    private String thumbnail;

    private String title;

    private String type;

    private Long duration;

    private String section;

    private String description;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private List<VideoTag> videoTagList;
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Long getDuration() {
        return duration;
    }

    public String getSection() {
        return section;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public List<VideoTag> getVideoTagList() {
        return videoTagList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setVideoTagList(List<VideoTag> videoTagList) {
        this.videoTagList = videoTagList;
    }
}
