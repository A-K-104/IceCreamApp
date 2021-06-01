package com.example.icecreamapplication;


import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private DateClass dateOfBirth;
    private boolean gender;//male=true // female= false
    private boolean admin;
    private Map<String, Object> mapOfOrders;
    private List<OrderIdentifier> listOfOrders;

    /**
     * first builder that gets basic user without orders and admin
     * by DEFAULT make the user NON admin
     * made for new user
     *
     * @param firstName   name of user
     * @param lastName    name of user
     * @param dateOfBirth date of birth of user
     * @param gender      and the function reformat mapOfUsers and listOfOrders.
     */
    public User(String firstName, String lastName, String dateOfBirth, boolean gender) {
        this.admin = false;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = new DateClass(dateOfBirth);
        this.gender = gender;
        this.mapOfOrders = new HashMap<>();
        this.listOfOrders = new ArrayList<>();
    }

    /**
     * second builder that gets basic user without orders
     * made for logging in
     *
     * @param firstName   name of user
     * @param lastName    name of user
     * @param dateOfBirth date of birth of user
     * @param gender      bool male = true, female = false
     * @param admin       if the user admin
     *                    and the function reformat mapOfUsers and listOfOrders.
     */
    public User(String firstName, String lastName, String dateOfBirth, boolean gender, boolean admin) {
        this.admin = admin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = new DateClass(dateOfBirth);
        this.gender = gender;
        this.mapOfOrders = new HashMap<>();
        this.listOfOrders = new ArrayList<>();
    }

    /**
     * third builder that gets basic user without orders
     * the function is depend on getUserMap() that conver map--> users
     *
     * @param userMap the function ask for map of users
     *                the map need to contain firstName, lastName,dateOfBirth, gender, admin.
     *                <p>
     *                and the function reformat mapOfUsers and listOfOrders.
     */
    public User(Map<String, Object> userMap) {
        User temp = setUserMap(userMap);
        this.firstName = temp.getFirstName();
        this.lastName = temp.getLastName();
        this.dateOfBirth = temp.getDateOfBirth();
        this.gender = temp.isGender();
        this.admin = temp.isAdmin();
        mapOfOrders = new HashMap<>();
        this.listOfOrders = new ArrayList<>();
    }


    public Map<String, Object> getUserMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fName", this.firstName);
        userMap.put("lName", this.lastName);
        userMap.put("dateOfBirth", this.dateOfBirth.getDate());
        userMap.put("gender", this.gender);
        userMap.put("admin", this.admin);
        return userMap;
    }

    /**
     * the function is depend on getUserMap() that conver map--> users
     * @param userMap the map MUST contain (string) firstName, (string) lastName, (DateClass) dateOfBirth, (boolean) gender, (boolean) admin.
     * @return basic User without orders
     */
    public User setUserMap(@NonNull Map<String, Object> userMap) {
        return new User(String.valueOf(userMap.get("fName")), String.valueOf(userMap.get("lName")),
                (String.valueOf(userMap.get("dateOfBirth"))), (boolean) userMap.get("gender"), (boolean) userMap.get("admin"));

    }

    /**
     * this function converts map of orders to list of orders
     * orders must named "order" with numbers start from 1 and MUST be sequential for ex: 1,2,3... and NEVER miss one number like 1,3,4...
     * @return List of orders
     */
    public List<OrderClass> getListOfOrdersFromMap() {
        List<OrderClass> tempOrdersList = new ArrayList<>();
        if (mapOfOrders != null) {
            if (mapOfOrders.get("order" + mapOfOrders.size()) != null) {
                for (int i = 0; i <= mapOfOrders.size(); i++) {
                    if (mapOfOrders.get("order" + i) != null) {
                        try {
                            Map<String, Object> temp = (Map<String, Object>) mapOfOrders.get("order" + i);
                            tempOrdersList.add(new OrderClass(temp));
                        } catch (Exception e) {
                            Log.d("userClass", "error: " + e);
                        }
                    }
                }
            }
        }
        return tempOrdersList;
    }
    /**
     * THIS IS THE SAME FUNCTION AS ABOVE. the different is that it is receiving a map of orders.
     * this function converts map of orders to list of orders
     * orders must named "order" with numbers start from 1 and MUST be sequential for ex: 1,2,3... and NEVER miss one number like 1,3,4...
     * @param mapOfOrders the map MUST contain (string) flavor, (DateClass) dateOfOrder, (string) nameOfOrder, (int) statusOfOrder
     * @return List of orders
     */
    public List<OrderClass> getListOfOrdersFromMap(Map<String, Object> mapOfOrders) {
        List<OrderClass> tempOrdersList = new ArrayList<>();
        if (mapOfOrders != null) {
            if (mapOfOrders.get("order" + mapOfOrders.size()) != null) {
                for (int i = 0; i <= mapOfOrders.size(); i++) {
                    if (new OrderClass((Map<String, Object>) mapOfOrders.get("order" + i)).getFlavor() != null) {
                        try {
                            tempOrdersList.add(new OrderClass((Map<String, Object>) mapOfOrders.get("order" + i)));
                        }catch (Exception e) {
                            Log.d("userClass", "error: " + e);
                        }
                    }
                }
            }
        }
        return tempOrdersList;
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

    public void addSingleOrderClass(OrderClass orderClass) {
        if(this.listOfOrders.size()>0) {
            this.listOfOrders.get(0).getOrderClasses().add(orderClass);
        }
        else {
            List<OrderClass>tempList = new ArrayList<>();
            tempList.add(orderClass);
            listOfOrders.add(new OrderIdentifier(tempList));
        }
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

    public List<OrderIdentifier> getListOfOrders() {
        return this.listOfOrders;
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth.getDate() +
                ", gender=" + gender +
                ", admin=" + admin +
                '}';
    }

}
