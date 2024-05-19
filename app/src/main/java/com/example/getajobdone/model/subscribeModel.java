package com.example.getajobdone.model;

public class subscribeModel {

    String spUid, businessName , active, email;

    public subscribeModel() {
    }

    public subscribeModel(String spUid, String businessName, String active, String email) {
        this.spUid = spUid;
        this.businessName = businessName;
        this.active = active;
        this.email = email;
    }

    public String getSpUid() {
        return spUid;
    }

    public void setSpUid(String spUid) {
        this.spUid = spUid;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
