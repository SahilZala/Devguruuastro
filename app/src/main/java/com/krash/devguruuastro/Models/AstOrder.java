package com.krash.devguruuastro.Models;

public class AstOrder {
    String UserUID, orderID, orderDate, duration, earning, name;
    String apayment,geteway,orderTime,orderType,tds;

    public AstOrder(String userUID, String orderID, String orderDate, String duration, String earning, String name) {
        UserUID = userUID;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.duration = duration;
        this.earning = earning;
        this.name = name;
    }

    public AstOrder(String userUID, String orderID, String orderDate, String duration, String earning, String name, String apayment, String geteway, String orderTime, String orderType, String tds) {
        UserUID = userUID;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.duration = duration;
        this.earning = earning;
        this.name = name;
        this.apayment = apayment;
        this.geteway = geteway;
        this.orderTime = orderTime;
        this.orderType = orderType;
        this.tds = tds;
    }

    public AstOrder() {
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
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

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApayment() {
        return apayment;
    }

    public void setApayment(String apayment) {
        this.apayment = apayment;
    }

    public String getGeteway() {
        return geteway;
    }

    public void setGeteway(String geteway) {
        this.geteway = geteway;
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

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }
}
