package com.example.icecreamapplication;


import android.util.Log;

import com.google.protobuf.StringValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User   implements Serializable {
    private String firstName;
    private String lastName;
    private DateClass dateOfBirth;
    private boolean gender;//male=true // female= false

    public User(String name,String lastName ,String dateOfBirth, boolean gender ) {

        this.firstName = name;
        this.lastName= lastName;
        this.dateOfBirth = new DateClass(dateOfBirth);
        this.gender = gender;

    }
    public User(Map<String,Object> userMap) {
    User temp = setUserMap(userMap);
        this.firstName = temp.getFirstName();
        this.lastName= temp.getLastName();
        this.dateOfBirth = temp.getDateOfBirth();
        this.gender = temp.isGender();
    }

    public Map<String,Object> getUserMap(){
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("fName",this.firstName);
        userMap.put("lName",this.lastName);
        userMap.put("dateOfBirth",this.dateOfBirth.getDate());
        userMap.put("gender",getGender());
        return userMap;
    }
    public User setUserMap(Map<String,Object> userMap){
        boolean bool = false;
        if((String.valueOf(userMap.get("gender")))=="1") {
            bool=true;
        }
        return new User(String.valueOf(userMap.get("fName")),String.valueOf(userMap.get("lName")),
                (String.valueOf(userMap.get("dateOfBirth"))),bool);

    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public int getGender() {
        if(gender)
            return 1;
        return 0;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", dateOfBirth=" + dateOfBirth.getDate() +
                ", gender=" + gender +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
