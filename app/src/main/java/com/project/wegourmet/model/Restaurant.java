package com.project.wegourmet.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurants", primaryKeys = {"hostId", "name"})
public class Restaurant {
    @NonNull
    String hostId;
    @NonNull
    String name;
    String address;
    String phone;
    String type;
    String mainImageUrl;

    public Restaurant(){}

    public Restaurant(String hostId, String name, String address, String phone,
                      String type) {
        this.hostId = hostId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.type = type;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }
}
