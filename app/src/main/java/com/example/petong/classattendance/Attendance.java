package com.example.petong.classattendance;

import com.google.firebase.Timestamp;

public class Attendance {
    private Timestamp date;
    private String studentID;
    private String courseID;

    public Attendance(Timestamp date, String studentID, String courseID) {
        this.date = date;
        this.studentID = studentID;
        this.courseID = courseID;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
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
