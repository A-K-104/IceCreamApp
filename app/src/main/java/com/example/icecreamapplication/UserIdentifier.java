package com.example.icecreamapplication;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserIdentifier implements Serializable {
    private List<OrderClass>orderClasses;
    private String userId;
//    private Map<String,Object>mapOfOrders;

    public UserIdentifier(List<OrderClass> orderClasses, String userId/*, Map<String, Object> mapOfOrders*/) {
        this.orderClasses = orderClasses;
        this.userId = userId;
//        this.mapOfOrders = mapOfOrders;
    }

    public List<OrderClass> getOrderClasses() {
        return orderClasses;
    }

    public void setOrderClasses(List<OrderClass> orderClasses) {
        this.orderClasses = orderClasses;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public Map<String, Object> getMapOfOrders() {
//        return mapOfOrders;
//    }
//
//    public void setMapOfOrders(Map<String, Object> mapOfOrders) {
//        this.mapOfOrders = mapOfOrders;
//    }

    @Override
    public String toString() {
        return "UserIdentifier{" +
                "orderClasses=" + orderClasses +
                ", userId='" + userId + '\'' +
//                ", mapOfOrders=" + mapOfOrders +
                '}';
    }
}
