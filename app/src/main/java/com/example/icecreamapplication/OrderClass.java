package com.example.icecreamapplication;

import java.io.Serializable;
import java.util.Map;

public class OrderClass  implements Serializable {

    private String flavor;
    private DateClass dateOfOrder;
    private int statusOfOrder;
    private String nameOfOrder;
    public String userId;

    public OrderClass(String flavor, DateClass dateOfOrder, int statusOfOrder,String nameOfOrder) {
        this.flavor = flavor;
        this.dateOfOrder = dateOfOrder;
        this.statusOfOrder = statusOfOrder;
        this.nameOfOrder=nameOfOrder;
    }
    public OrderClass(String flavor, int statusOfOrder,String nameOfOrder) {
        this.flavor = flavor;
        this.dateOfOrder = new DateClass();
        this.statusOfOrder = statusOfOrder;
        this.nameOfOrder=nameOfOrder;
    }
    public OrderClass(String flavor,String nameOfOrder) {
        this.flavor = flavor;
        this.nameOfOrder = nameOfOrder;
        this.dateOfOrder = new DateClass();
    }
    public OrderClass(Map<String,Object> map){
        if(map!=null) {
            OrderClass temp = getOrderMap(map);
            this.flavor = temp.getFlavor();
            this.dateOfOrder = temp.getDateOfOrder();
            this.statusOfOrder = temp.getStatusOfOrder();
            this.nameOfOrder = temp.getNameOfOrder();
        }
    }
public OrderClass getOrderMap(Map<String,Object> orderMap){
//    return new OrderClass(String.valueOf(orderMap.get("flavor")),
    if(orderMap!=null) {
        Map<String, Object> tempMapOfDate = (Map<String, Object>) orderMap.get("dateOfOrder");
        return new OrderClass(String.valueOf(orderMap.get("flavor")),
                new DateClass((String) tempMapOfDate.get("date")),
                Integer.parseInt(String.valueOf(orderMap.get("statusOfOrder"))),
                String.valueOf(orderMap.get("nameOfOrder")));
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

    public String getNameOfOrder() {
        return nameOfOrder;
    }

    public void setNameOfOrder(String nameOfOrder) {
        this.nameOfOrder = nameOfOrder;
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
                ", nameOfOrder=" + nameOfOrder+
                '}';
    }
}
