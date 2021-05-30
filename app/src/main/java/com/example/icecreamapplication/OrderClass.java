package com.example.icecreamapplication;

import java.io.Serializable;
import java.util.Map;

public class OrderClass  implements Serializable {

    private String flavor;
    private DateClass dateOfOrder;
    private int statusOfOrder;
    public String userId;

    public OrderClass(String flavor, DateClass dateOfOrder, int statusOfOrder) {
        this.flavor = flavor;
        this.dateOfOrder = dateOfOrder;
        this.statusOfOrder = statusOfOrder;
    }
    public OrderClass(String flavor, int statusOfOrder) {
        this.flavor = flavor;
        this.dateOfOrder = new DateClass();
        this.statusOfOrder = statusOfOrder;
    }
    public OrderClass(String flavor) {
        this.flavor = flavor;
        this.dateOfOrder = new DateClass();
    }
    public OrderClass(Map<String,Object> map){
        if(map!=null) {
            OrderClass temp = getOrderMap(map);
            this.flavor = temp.getFlavor();
            this.dateOfOrder = temp.getDateOfOrder();
            this.statusOfOrder = temp.getStatusOfOrder();
        }
    }
public OrderClass getOrderMap(Map<String,Object> orderMap){
//    return new OrderClass(String.valueOf(orderMap.get("flavor")),
    if(orderMap!=null) {
        Map<String, Object> tempMapOfDate = (Map<String, Object>) orderMap.get("dateOfOrder");
        return new OrderClass(String.valueOf(orderMap.get("flavor")),
                new DateClass((String) tempMapOfDate.get("date")),
                Integer.parseInt(String.valueOf(orderMap.get("statusOfOrder"))));
    }
    return null;
}
    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public DateClass getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(DateClass dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public int getStatusOfOrder() {
        return statusOfOrder;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatusOfOrderString() {
        if(this.statusOfOrder ==0)
            return "order received";
        if(this.statusOfOrder ==1)
            return "order in preparation";
        if(this.statusOfOrder ==2)
            return "order in shipping";
        if(this.statusOfOrder ==3)
            return "order arrived";
        return "error";
    }

    public void setStatusOfOrder(int statusOfOrder) {
        this.statusOfOrder = statusOfOrder;
    }

    @Override
    public String toString() {
        return "OrderClass{" +
                "flavor='" + flavor + '\'' +
                ", dateOfOrder=" + dateOfOrder +
                ", statusOfOrder=" + statusOfOrder +
                '}';
    }
}
