package com.example.icecreamapplication;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OrderIdentifier implements Serializable {
    private List<OrderClass>orderClasses;
    private String userId;

    public OrderIdentifier(List<OrderClass> orderClasses, String userId/*, Map<String, Object> mapOfOrders*/) {
        this.orderClasses = orderClasses;
        this.userId = userId;
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

    @Override
    public String toString() {
        return "UserIdentifier{" +
                "orderClasses=" + orderClasses +
                ", userId='" + userId + '\'' +
                '}';
    }
}
