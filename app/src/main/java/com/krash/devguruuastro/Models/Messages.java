package com.krash.devguruuastro.Models;

public class Messages {
    String mid,astroid,userid,sender,type,message,url,date,time,activation;

    public Messages(String mid, String astroid, String userid, String sender, String type, String message, String url, String date, String time, String activation) {
        this.mid = mid;
        this.astroid = astroid;
        this.userid = userid;
        this.sender = sender;
        this.type = type;
        this.message = message;
        this.url = url;
        this.date = date;
        this.time = time;
        this.activation = activation;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Messages() {
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAstroid() {
        return astroid;
    }

    public void setAstroid(String astroid) {
        this.astroid = astroid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}
