package com.example.petong.classattendance;

import java.util.List;
import java.util.Map;

public class Student {
    private String prename;
    private String firstname;
    private String lastname;
    private Map<String, String> courseID;

    public Student(String prename, String firstname, String lastname, Map<String, String> courseID) {
        this.prename = prename;
        this.firstname = firstname;
        this.lastname = lastname;
        this.courseID = courseID;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
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

    public Map<String, String> getCourseID() {
        return courseID;
    }

    public void setCourseID(Map<String, String> courseID) {
        this.courseID = courseID;
    }
}