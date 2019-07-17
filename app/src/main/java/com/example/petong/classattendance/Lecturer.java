package com.example.petong.classattendance;

import java.util.Map;

public class Lecturer {
    private String firstname;
    private String lastname;
    //private Map<String, String> courseID;

    public Lecturer() { }
    /*
    public Lecturer(Map<String, String> courseID) {
        this.courseID = courseID;
    }*/

    public Lecturer(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
/*
    public Map<String, String> getCourseID() {
        return courseID;
    }

    public void setCourseID(Map<String, String> courseID) {
        this.courseID = courseID;
    }*/
}