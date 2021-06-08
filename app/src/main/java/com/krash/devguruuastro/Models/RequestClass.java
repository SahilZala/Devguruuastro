package com.krash.devguruuastro.Models;

public class RequestClass {
    String astrologerid,userid,requestid,sessionid,username,photo,duration,time,date,status,activation,reqtype;

    public RequestClass() {
    }

    public RequestClass(String astrologerid, String userid, String requestid, String sessionid, String username, String photo, String duration, String time, String date, String status, String activation, String reqtype) {
        this.astrologerid = astrologerid;
        this.userid = userid;
        this.requestid = requestid;
        this.sessionid = sessionid;
        this.username = username;
        this.photo = photo;
        this.duration = duration;
        this.time = time;
        this.date = date;
        this.status = status;
        this.activation = activation;
        this.reqtype = reqtype;
    }

    public String getAstrologerid() {
        return astrologerid;
    }

    public void setAstrologerid(String astrologerid) {
        this.astrologerid = astrologerid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getReqtype() {
        return reqtype;
    }

    public void setReqtype(String reqtype) {
        this.reqtype = reqtype;
    }
}
