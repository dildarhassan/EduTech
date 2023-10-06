package com.example.edutech.Model_Activity;


public class Video_model {

    public String getVideoDesk() {
        return videoDesk;
    }

    public void setVideoDesk(String videoDesk) {
        this.videoDesk = videoDesk;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }


    public Video_model(String videoName) {
        this.videoName = videoName;
    }

    private  String videoName;

    private String videoDesk;

    public Video_model(String videoName, String videoUrl) {
        this.videoName = videoName;
        this.videoUrl = videoUrl;
    }

    private String videoUrl;


    public Video_model(String videoDesk, String videoUrl, String video) {
        this.videoDesk = videoDesk;
        this.videoUrl = videoUrl;
        this.videoName = video;
    }

}
