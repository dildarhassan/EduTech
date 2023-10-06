package com.example.edutech.Model_Activity;

public class UserDataModel {
    private String Name;
    private String email;
    private String mobile_no;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public UserDataModel(String name, String email, String mobile_no) {
        Name = name;
        this.email = email;
        this.mobile_no = mobile_no;
    }
}
