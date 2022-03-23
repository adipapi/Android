package com.project.wegourmet.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import java.util.Map;

@Entity(tableName = "posts")
public class Post {
    @PrimaryKey
    @NonNull
    String id;
    String restaurantId;
    String imageUrl;
    String text;
    Boolean isDeleted;
    Long updateDate;

    public Post(){}

    public Post(String restaurantId, String text, Boolean isDeleted) {
        this.restaurantId = restaurantId;
        this.text = text;
        this.isDeleted = isDeleted;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String restaurantId = (String) json.get("restaurantId");
        String imageUrl = (String) json.get("imageUrl");
        Boolean isDeleted = (Boolean) json.get("deleted");
        String text = (String) json.get("text");
//        Timestamp ts = (Timestamp)json.get("updateDate");
//        Long updateDate = ts.getSeconds();

        Post post = new Post(restaurantId, text, isDeleted);
        post.setId(id);
//        post.setUpdateDate(updateDate);
        post.setImageUrl(imageUrl);
        return post;
    }

}
