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
    String restaurantName;
    String imageUrl;
    String text;
    Long updateDate;

    public Post(){}

    public Post(String id, String restaurantName, String text) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.text = text;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
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

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String restaurantName = (String) json.get("restaurantName");
        String imageUrl = (String) json.get("imageUrl");
        String text = (String) json.get("text");
//        Timestamp ts = (Timestamp)json.get("updateDate");
//        Long updateDate = ts.getSeconds();

        Post post = new Post(id, restaurantName, text);
//        post.setUpdateDate(updateDate);
        post.setImageUrl(imageUrl);
        return post;
    }

}
