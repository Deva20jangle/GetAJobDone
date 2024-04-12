package com.example.getajobdone.model;

public class userModel {
    String uID, userType, name, email, contactNo, address, password, active, businessName, spID;

    public userModel() {
    }

    public userModel(String uID, String userType, String name, String email, String contactNo, String address, String password, String active, String businessName, String spID) {
        this.uID = uID;
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.address = address;
        this.password = password;
        this.active = active;
        this.businessName = businessName;
        this.spID = spID;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getSpID() {
        return spID;
    }

    public void setSpID(String spID) {
        this.spID = spID;
    }
}