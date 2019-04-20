package com.example.petong.classattendance;

public class Student {
    private String firstname;
    private String lastname;
    private String phone;

    public Student() {}
    public Student(String firstname, String lastname, String phone) {
        // ...
    }
    public String getFirstname(){
        return firstname;
    }
    public String getLastname(){
        return lastname;
    }
    public String getPhone(){
        return phone;
    }
}