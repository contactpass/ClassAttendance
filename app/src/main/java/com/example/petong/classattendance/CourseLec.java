package com.example.petong.classattendance;

import java.util.HashMap;
import java.util.Map;

public class CourseLec {
    private String courseID;
    private String title;
    private String day;
    private String time;
    private boolean status;
    private HashMap<String, String> sectionLec;
    private HashMap<String, String> sectionRoom;
    private HashMap<String, String> studenntID;

    public CourseLec() {}

    public CourseLec(String courseID, String title, String day, String time, boolean status, HashMap<String, String> sectionLec, HashMap<String, String> sectionRoom, HashMap<String, String> studentID) {
        this.courseID = courseID;
        this.title = title;
        this.day = day;
        this.time = time;
        this.status = status;
        this.sectionLec = sectionLec;
        this.sectionRoom = sectionRoom;
        this.studenntID = studentID;
    }

    public HashMap<String, String> getSectionRoom() {
        return sectionRoom;
    }

    public HashMap<String, String> getStudenntID() {
        return studenntID;
    }

    public void setStudenntID(HashMap<String, String> studenntID) {
        this.studenntID = studenntID;
    }

    public void setSectionRoom(HashMap<String, String> sectionRoom) {
        this.sectionRoom = sectionRoom;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public HashMap<String, String> getSectionLec() { return sectionLec; }

    public void setSectionLec(HashMap<String, String> sectionLec) { this.sectionLec = sectionLec; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
