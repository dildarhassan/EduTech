package com.example.edutech.Model_Activity;

public class Courses_model {
    public Courses_model(String courseName, String courseImageUrl) {
        this.courseName = courseName;
        this.courseImageUrl = courseImageUrl;
    }

    private  String courseName;
    private  String courseImageUrl;
    private  String courseDesk;


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }

    public void setCourseImageUrl(String courseImageUrl) {
        this.courseImageUrl = courseImageUrl;
    }

    public String getCourseDesk() {
        return courseDesk;
    }

    public void setCourseDesk(String courseDesk) {
        this.courseDesk = courseDesk;
    }


    Courses_model(String courseName, String courseImageUrl, String courseDesk){
        this.courseName=courseName;
        this.courseImageUrl=courseImageUrl;
        this.courseDesk=courseDesk;
    }

}
