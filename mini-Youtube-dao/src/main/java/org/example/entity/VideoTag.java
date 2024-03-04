package org.example.entity;

import java.time.LocalDateTime;

public class VideoTag {
    private Long id;
    private Long videoId;
    private Long tagId;
    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Long getTagId() {
        return tagId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
