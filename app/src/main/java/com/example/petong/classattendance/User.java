package com.example.petong.classattendance;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private UserData data;

    public User(String status, UserData data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
