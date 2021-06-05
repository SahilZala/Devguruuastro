package com.krash.devguruuastro.Models;

public class AstrologerModel {
    String uid, profileURL, name, email, password, address, mobile, dob, bachD, bachURL, mastD, mastURL, otherQ, skills, skillURL, experience, longB, workD, accountD, aadhaarNO, aadharURL, panNO, panURL, language, father, link, hoursSpend, chatOnline, callOnline, chatPrice, callPrice, chatMins, callMins, reports, isVerified, isBusy, balance,holdername,ifsccode,checkURL,token;

    String photocount;

    public AstrologerModel(String uid, String profileURL, String name, String email, String password, String address, String mobile, String dob, String bachD, String bachURL, String mastD, String mastURL, String otherQ, String skills, String skillURL, String experience, String longB, String workD, String accountD, String aadhaarNO, String aadharURL, String panNO, String panURL, String language, String father, String link, String hoursSpend, String chatOnline, String callOnline, String chatPrice, String callPrice, String chatMins, String callMins, String reports, String isVerified, String isBusy, String balance, String holdername, String ifsccode, String checkURL, String token, String photocount) {
        this.uid = uid;
        this.profileURL = profileURL;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.dob = dob;
        this.bachD = bachD;
        this.bachURL = bachURL;
        this.mastD = mastD;
        this.mastURL = mastURL;
        this.otherQ = otherQ;
        this.skills = skills;
        this.skillURL = skillURL;
        this.experience = experience;
        this.longB = longB;
        this.workD = workD;
        this.accountD = accountD;
        this.aadhaarNO = aadhaarNO;
        this.aadharURL = aadharURL;
        this.panNO = panNO;
        this.panURL = panURL;
        this.language = language;
        this.father = father;
        this.link = link;
        this.hoursSpend = hoursSpend;
        this.chatOnline = chatOnline;
        this.callOnline = callOnline;
        this.chatPrice = chatPrice;
        this.callPrice = callPrice;
        this.chatMins = chatMins;
        this.callMins = callMins;
        this.reports = reports;
        this.isVerified = isVerified;
        this.isBusy = isBusy;
        this.balance = balance;
        this.holdername = holdername;
        this.ifsccode = ifsccode;
        this.checkURL = checkURL;
        this.token = token;
        this.photocount = photocount;
    }

    public AstrologerModel(String uid, String profileURL, String name, String email, String password, String address, String mobile, String dob, String bachD, String bachURL, String mastD, String mastURL, String otherQ, String skills, String skillURL, String experience, String longB, String workD, String accountD, String aadhaarNO, String aadharURL, String panNO, String panURL, String language, String father, String link, String hoursSpend, String chatOnline, String callOnline, String chatPrice, String callPrice, String chatMins, String callMins, String reports, String isVerified, String isBusy, String balance, String holdername, String ifsccode, String checkURL, String token) {
        this.uid = uid;
        this.profileURL = profileURL;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.dob = dob;
        this.bachD = bachD;
        this.bachURL = bachURL;
        this.mastD = mastD;
        this.mastURL = mastURL;
        this.otherQ = otherQ;
        this.skills = skills;
        this.skillURL = skillURL;
        this.experience = experience;
        this.longB = longB;
        this.workD = workD;
        this.accountD = accountD;
        this.aadhaarNO = aadhaarNO;
        this.aadharURL = aadharURL;
        this.panNO = panNO;
        this.panURL = panURL;
        this.language = language;
        this.father = father;
        this.link = link;
        this.hoursSpend = hoursSpend;
        this.chatOnline = chatOnline;
        this.callOnline = callOnline;
        this.chatPrice = chatPrice;
        this.callPrice = callPrice;
        this.chatMins = chatMins;
        this.callMins = callMins;
        this.reports = reports;
        this.isVerified = isVerified;
        this.isBusy = isBusy;
        this.balance = balance;
        this.holdername = holdername;
        this.ifsccode = ifsccode;
        this.checkURL = checkURL;
        this.token = token;
    }

    public AstrologerModel(String uid, String profileURL, String name, String email, String password, String address, String mobile, String dob, String bachD, String bachURL, String mastD, String mastURL, String otherQ, String skills, String skillURL, String experience, String longB, String workD, String accountD, String aadhaarNO, String aadharURL, String panNO, String panURL, String language, String father, String link, String hoursSpend, String chatOnline, String callOnline, String chatPrice, String callPrice, String chatMins, String callMins, String reports, String isVerified, String isBusy, String balance, String holdername, String ifsccode, String checkURL) {
        this.uid = uid;
        this.profileURL = profileURL;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.dob = dob;
        this.bachD = bachD;
        this.bachURL = bachURL;
        this.mastD = mastD;
        this.mastURL = mastURL;
        this.otherQ = otherQ;
        this.skills = skills;
        this.skillURL = skillURL;
        this.experience = experience;
        this.longB = longB;
        this.workD = workD;
        this.accountD = accountD;
        this.aadhaarNO = aadhaarNO;
        this.aadharURL = aadharURL;
        this.panNO = panNO;
        this.panURL = panURL;
        this.language = language;
        this.father = father;
        this.link = link;
        this.hoursSpend = hoursSpend;
        this.chatOnline = chatOnline;
        this.callOnline = callOnline;
        this.chatPrice = chatPrice;
        this.callPrice = callPrice;
        this.chatMins = chatMins;
        this.callMins = callMins;
        this.reports = reports;
        this.isVerified = isVerified;
        this.isBusy = isBusy;
        this.balance = balance;
        this.holdername = holdername;
        this.ifsccode = ifsccode;
        this.checkURL = checkURL;
    }

    public String getHoldername() {
        return holdername;
    }

    public void setHoldername(String holdername) {
        this.holdername = holdername;
    }

    public String getIfsccode() {
        return ifsccode;
    }

    public void setIfsccode(String ifsccode) {
        this.ifsccode = ifsccode;
    }

    public String getCheckURL() {
        return checkURL;
    }

    public void setCheckURL(String checkURL) {
        this.checkURL = checkURL;
    }

    public AstrologerModel() {
    }

    public String getPhotocount() {
        return photocount;
    }

    public void setPhotocount(String photocount) {
        this.photocount = photocount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBachD() {
        return bachD;
    }

    public void setBachD(String bachD) {
        this.bachD = bachD;
    }

    public String getBachURL() {
        return bachURL;
    }

    public void setBachURL(String bachURL) {
        this.bachURL = bachURL;
    }

    public String getMastD() {
        return mastD;
    }

    public void setMastD(String mastD) {
        this.mastD = mastD;
    }

    public String getMastURL() {
        return mastURL;
    }

    public void setMastURL(String mastURL) {
        this.mastURL = mastURL;
    }

    public String getOtherQ() {
        return otherQ;
    }

    public void setOtherQ(String otherQ) {
        this.otherQ = otherQ;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSkillURL() {
        return skillURL;
    }

    public void setSkillURL(String skillURL) {
        this.skillURL = skillURL;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLongB() {
        return longB;
    }

    public void setLongB(String longB) {
        this.longB = longB;
    }

    public String getWorkD() {
        return workD;
    }

    public void setWorkD(String workD) {
        this.workD = workD;
    }

    public String getAccountD() {
        return accountD;
    }

    public void setAccountD(String accountD) {
        this.accountD = accountD;
    }

    public String getAadhaarNO() {
        return aadhaarNO;
    }

    public void setAadhaarNO(String aadhaarNO) {
        this.aadhaarNO = aadhaarNO;
    }

    public String getAadharURL() {
        return aadharURL;
    }

    public void setAadharURL(String aadharURL) {
        this.aadharURL = aadharURL;
    }

    public String getPanNO() {
        return panNO;
    }

    public void setPanNO(String panNO) {
        this.panNO = panNO;
    }

    public String getPanURL() {
        return panURL;
    }

    public void setPanURL(String panURL) {
        this.panURL = panURL;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getHoursSpend() {
        return hoursSpend;
    }

    public void setHoursSpend(String hoursSpend) {
        this.hoursSpend = hoursSpend;
    }

    public String getChatOnline() {
        return chatOnline;
    }

    public void setChatOnline(String chatOnline) {
        this.chatOnline = chatOnline;
    }

    public String getCallOnline() {
        return callOnline;
    }

    public void setCallOnline(String callOnline) {
        this.callOnline = callOnline;
    }

    public String getChatPrice() {
        return chatPrice;
    }

    public void setChatPrice(String chatPrice) {
        this.chatPrice = chatPrice;
    }

    public String getCallPrice() {
        return callPrice;
    }

    public void setCallPrice(String callPrice) {
        this.callPrice = callPrice;
    }

    public String getChatMins() {
        return chatMins;
    }

    public void setChatMins(String chatMins) {
        this.chatMins = chatMins;
    }

    public String getCallMins() {
        return callMins;
    }

    public void setCallMins(String callMins) {
        this.callMins = callMins;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(String isBusy) {
        this.isBusy = isBusy;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
