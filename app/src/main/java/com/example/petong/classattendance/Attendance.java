package com.example.petong.classattendance;

import com.google.firebase.Timestamp;

public class Attendance {
    private String date;
    private String studentID;
    private String courseID;
    private String fullname;

    public Attendance() {}

    public Attendance(String date, String studentID, String courseID, String fullname) {
        this.date = date;
        this.studentID = studentID;
        this.courseID = courseID;
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
}
