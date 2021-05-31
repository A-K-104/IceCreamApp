package com.example.icecreamapplication;


import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User   implements Serializable {
    private String firstName;
    private String lastName;
    private DateClass dateOfBirth;
    private boolean gender;//male=true // female= false
    private Map<String,Object>mapOfOrders;
    private boolean admin;
    private List<OrderClass> orderClasses;
    private List<OrderIdentifier> listOfOrders;

    public User(String name,String lastName ,String dateOfBirth, boolean gender ) {
        this.admin = false;
        this.firstName = name;
        this.lastName= lastName;
        this.dateOfBirth = new DateClass(dateOfBirth);
        this.gender = gender;
        mapOfOrders = new HashMap<>();
        this.orderClasses = new ArrayList<>();
        this.listOfOrders = new ArrayList<>();

    }
    public User(String name,String lastName ,String dateOfBirth, boolean gender,boolean admin ) {
        this.admin = admin;
        this.firstName = name;
        this.lastName= lastName;
        this.dateOfBirth = new DateClass(dateOfBirth);
        this.gender = gender;
        mapOfOrders = new HashMap<>();
        this.orderClasses = new ArrayList<>();
        this.listOfOrders = new ArrayList<>();
    }
    public User(Map<String,Object> userMap) {
    User temp = setUserMap(userMap);
        this.firstName = temp.getFirstName();
        this.lastName= temp.getLastName();
        this.dateOfBirth = temp.getDateOfBirth();
        this.gender = temp.isGender();
        this.admin = temp.isAdmin();
        mapOfOrders = new HashMap<>();
        this.orderClasses = new ArrayList<>();
        this.listOfOrders = new ArrayList<>();
    }

    public Map<String,Object> getUserMap(){
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("fName",this.firstName);
        userMap.put("lName",this.lastName);
        userMap.put("dateOfBirth",this.dateOfBirth.getDate());
        userMap.put("gender",this.gender);
        userMap.put("admin",this.admin);
        return userMap;
    }
    public User setUserMap(Map<String,Object> userMap){
        return new User(String.valueOf(userMap.get("fName")),String.valueOf(userMap.get("lName")),
                (String.valueOf(userMap.get("dateOfBirth"))),(boolean) userMap.get("gender"),(boolean) userMap.get("admin"));

    }
    public OrderClass getLastOrderClass () {
        if (mapOfOrders != null)
            return new OrderClass((Map<String, Object>) mapOfOrders.get("order" + mapOfOrders.size()));
        return null;
    }
    public List<OrderClass> getListOfOrdersFromList(){

        List<OrderClass> myList = new ArrayList<>();
        if(mapOfOrders!=null) {
            if (mapOfOrders.get("order" + mapOfOrders.size()) != null) {
                for (int i = 0; i <= mapOfOrders.size(); i++) {
                    if (mapOfOrders.get("order" + i) != null) {
                        try {
                            Map<String, Object> temp = (Map<String, Object>) mapOfOrders.get("order" + i);
                            myList.add(new OrderClass(temp));
                        } catch (Exception e) {
                            Log.d("TAG", "error: " + e);

                        }
                    }
                }
            }
        }
        return myList;
    }
    public List<OrderClass> getListOfOrdersFromMap(Map<String,Object>mapOfOrders){
        List<OrderClass> myList = new ArrayList<>();
        if (mapOfOrders.get("order" + mapOfOrders.size()) != null){
            for (int i =0;i<=mapOfOrders.size();i++) {
                if(new OrderClass((Map<String, Object>) mapOfOrders.get("order" +i)).getFlavor()!=null)
                myList.add(new OrderClass((Map<String, Object>) mapOfOrders.get("order" +i)));
            }
        }
        return myList;
    }
    public Map<String, Object> getMapOfOrders() {
        return mapOfOrders;
    }

    public void setMapOfOrders(Map<String, Object> mapOfOrders) {
        this.mapOfOrders = mapOfOrders;
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

    public void addSingleOrderClass(OrderClass orderClass){
        this.orderClasses.add(orderClass);
    }

    public List<OrderClass> getOrderClasses() {
        return orderClasses;
    }

    public void setOrderClasses(List<OrderClass> orderClasses) {
        this.orderClasses = orderClasses;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setListOfOrders(List<OrderIdentifier> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }
    public List<OrderIdentifier> getListOfOrders(){
        return this.listOfOrders;
    }



    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth.getDate() +
                ", gender=" + gender +
//                ", mapOfOrders=" + mapOfOrders +
                ", admin=" + admin +
                '}';
    }

}
