package com.example.petong.classattendance;

import java.util.List;
import java.util.Map;

public class Student {
    private String studentID;
    private String prename;
    private String firstname;
    private String lastname;
    private Map<String, String> courseID;
    private boolean status;

    public Student() {}

    public Student(String studentID, String prename, String firstname, String lastname, Map<String, String> courseID, boolean status) {
        this.studentID = studentID;
        this.prename = prename;
        this.firstname = firstname;
        this.lastname = lastname;
        this.courseID = courseID;
        this.status = status;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
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

    public boolean getStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }
}