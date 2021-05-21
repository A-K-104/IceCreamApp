package com.example.icecreamapplication;

public class User {
    private String userName;
    private DateClass dateOfBirth;
    private boolean gender;//male=true // female= false
    private String userId;

    public User(String userName, String dateOfBirth, boolean gender, String userId) {
        this.userName = userName;
        this.dateOfBirth = new DateClass(dateOfBirth);
        this.gender = gender;
        this.userId = userId;
    }
    public User(String userName, String dateOfBirth, boolean gender) {
        this.userName = userName;
        this.dateOfBirth = new DateClass(dateOfBirth);
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DateClass getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateClass dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", userId='" + userId + '\'' +
                '}';
    }
}
