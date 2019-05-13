package com.example.petong.classattendance;

public class LoginUser {
    private String UserName;
    private String Password;

    public LoginUser(){ }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
