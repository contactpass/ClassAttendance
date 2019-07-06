package com.example.petong.classattendance;

public class Course {
    private String title;
    private String section_lec;
    private String section_lab;
    private String credit_lec;
    private String credit_lab;
    private String day;
    private String time;
    private String type;

    public Course(String title, String section_lec, String section_lab, String credit_lec, String creddit_lab, String day, String time, String type) {
        this.title = title;
        this.section_lec = section_lec;
        this.section_lab = section_lab;
        this.credit_lec = credit_lec;
        this.credit_lab = creddit_lab;
        this.day = day;
        this.time = time;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection_lec() {
        return section_lec;
    }

    public void setSection_lec(String section_lec) {
        this.section_lec = section_lec;
    }

    public String getSection_lab() {
        return section_lab;
    }

    public void setSection_lab(String section_lab) {
        this.section_lab = section_lab;
    }

    public String getCredit_lec() {
        return credit_lec;
    }

    public void setCredit_lec(String credit_lec) {
        this.credit_lec = credit_lec;
    }

    public String getCredit_lab() {
        return credit_lab;
    }

    public void setCredit_lab(String credit_lab) {
        this.credit_lab = credit_lab;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
