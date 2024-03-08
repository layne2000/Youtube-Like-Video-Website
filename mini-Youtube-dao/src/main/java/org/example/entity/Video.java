package org.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document(indexName = "videos")
public class Video {
    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long userId;

    private String url;

    private String thumbnail;

    @Field(type = FieldType.Text)
    private String title;

    private String type;

    private Long duration;

    private String section;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Date)
    private LocalDateTime createdTime;

    @Field(type = FieldType.Date)
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
