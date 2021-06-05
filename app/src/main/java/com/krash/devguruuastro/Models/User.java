package com.krash.devguruuastro.Models;

public class User {
    String uid, email, name, phoneNo, profileImage, DOB, TOB, POB, Occupation, Gender, MaritalStatus, status, duration, balance;

    RequestClass rc;

    public User() {
    }

    public User(String uid, String email, String name, String phoneNo, String profileImage, String DOB, String TOB, String POB, String occupation, String gender, String maritalStatus, String status, String duration, String balance, RequestClass rc) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.phoneNo = phoneNo;
        this.profileImage = profileImage;
        this.DOB = DOB;
        this.TOB = TOB;
        this.POB = POB;
        Occupation = occupation;
        Gender = gender;
        MaritalStatus = maritalStatus;
        this.status = status;
        this.duration = duration;
        this.balance = balance;
        this.rc = rc;
    }



    public User(String uid, String email, String name, String phoneNo, String profileImage, String status, String duration, String balance) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.phoneNo = phoneNo;
        this.profileImage = profileImage;
        this.status = status;
        this.balance = balance;
        this.duration = duration;
    }

    public User(String uid, String name, String phoneNo, String profileImage, String DOB, String TOB, String POB, String occupation, String Gender, String MaritalStatus) {
        this.uid = uid;
        this.name = name;
        this.phoneNo = phoneNo;
        this.profileImage = profileImage;
        this.DOB = DOB;
        this.TOB = TOB;
        this.POB = POB;
        this.Occupation = occupation;
        this.Gender = Gender;
        this.MaritalStatus = MaritalStatus;
    }

    public RequestClass getRc() {
        return rc;
    }

    public void setRc(RequestClass rc) {
        this.rc = rc;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getTOB() {
        return TOB;
    }

    public void setTOB(String TOB) {
        this.TOB = TOB;
    }

    public String getPOB() {
        return POB;
    }

    public void setPOB(String POB) {
        this.POB = POB;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
