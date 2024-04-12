package com.example.getajobdone.model;

public class serviceModel {
    String serviceID, spUid, spName, businessAddress, businessContactNo, businessName, serviceType, servicePrice, serviceDescription, serviceRating, serviceRatingSum, serviceRatingCount, bankAccountHolderName, bankAccountNumber, bankIFSCCode, bankUPI;

    public serviceModel() {
    }

    public serviceModel(String serviceID, String spUid, String spName, String businessAddress, String businessContactNo, String businessName, String serviceType, String servicePrice, String serviceDescription, String serviceRating, String serviceRatingSum, String serviceRatingCount, String bankAccountHolderName, String bankAccountNumber, String bankIFSCCode, String bankUPI) {
        this.serviceID = serviceID;
        this.spUid = spUid;
        this.spName = spName;
        this.businessAddress = businessAddress;
        this.businessContactNo = businessContactNo;
        this.businessName = businessName;
        this.serviceType = serviceType;
        this.servicePrice = servicePrice;
        this.serviceDescription = serviceDescription;
        this.serviceRating = serviceRating;
        this.serviceRatingCount = serviceRatingCount;
        this.serviceRatingSum = serviceRatingSum;
        this.bankAccountHolderName = bankAccountHolderName;
        this.bankAccountNumber = bankAccountNumber;
        this.bankIFSCCode = bankIFSCCode;
        this.bankUPI = bankUPI;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getSpUid() {
        return spUid;
    }

    public void setSpUid(String spUid) {
        this.spUid = spUid;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessContactNo() {
        return businessContactNo;
    }

    public void setBusinessContactNo(String businessContactNo) {
        this.businessContactNo = businessContactNo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(String serviceRating) {
        this.serviceRating = serviceRating;
    }

    public String getServiceRatingSum() {
        return serviceRatingSum;
    }

    public void setServiceRatingSum(String serviceRatingSum) {
        this.serviceRatingSum = serviceRatingSum;
    }

    public String getServiceRatingCount() {
        return serviceRatingCount;
    }

    public void setServiceRatingCount(String serviceRatingCount) {
        this.serviceRatingCount = serviceRatingCount;
    }
}
