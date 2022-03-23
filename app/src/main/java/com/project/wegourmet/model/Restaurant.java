package com.project.wegourmet.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "restaurants", primaryKeys = {"hostId", "name"})
public class Restaurant {
    final public static String COLLECTION_NAME = "restaurants";
    @PrimaryKey
    @NonNull
    String id = "";
    @NonNull
    String hostId ="";
    @NonNull
    String name="";
    String address="";
    String phone="";
    String type="";
    String mainImageUrl="";
    String description="";
    Long updateDate= new Long(0);

    public Restaurant(){}

    public Restaurant(String id,String hostId, String name, String address, String phone,
                      String type, String description) {
        this.id = id;
        this.hostId = hostId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.type = type;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("hostId",id);
        json.put("name",name);
        json.put("phone",phone);
        json.put("type",type);
        json.put("mainImageUrl",mainImageUrl);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("mainImageUrl",description);
        return json;
    }

    public static Restaurant create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String hostId = (String) json.get("hostId");
        String name = (String) json.get("name");
        String address = (String) json.get("address");
        String phone = (String) json.get("phone");
        String type = (String) json.get("type");
        String description = (String) json.get("description");
        String mainImageUrl = (String) json.get("mainImageUrl");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        Restaurant restaurant = new Restaurant(id,hostId,name,address,phone,type,description);
        restaurant.setUpdateDate(updateDate);
        restaurant.setMainImageUrl(mainImageUrl);
        return restaurant;
    }
}
