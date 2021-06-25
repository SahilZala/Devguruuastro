package com.krash.devguruuastros.Models;

public class UserOrder {
    String AstUID, orderID, orderDate, duration, expense, name , orderTime, orderType;

    public UserOrder(String astUID, String orderID, String orderDate, String duration, String expense, String name, String orderTime, String orderType) {
        AstUID = astUID;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.duration = duration;
        this.expense = expense;
        this.name = name;
        this.orderTime = orderTime;
        this.orderType = orderType;
    }

    public UserOrder() {
    }

    public String getAstUID() {
        return AstUID;
    }

    public void setAstUID(String astUID) {
        AstUID = astUID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
