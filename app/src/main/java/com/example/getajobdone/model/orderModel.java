package com.example.getajobdone.model;

public class orderModel {

    String serviceId, serviceType, servicePrice, serviceDesc, businessName, SPContactNo, spUid, address, spName, orderId, customerUid, customerName, customerContact, orderStatus, orderDesc, date, orderStatusDesc, orderFeedback;

    public orderModel() {
    }

    public orderModel(String serviceId, String serviceType, String servicePrice, String serviceDesc, String businessName, String SPContactNo, String spUid, String address, String spName, String orderId, String customerUid, String customerName, String customerContact, String orderStatus, String orderDesc, String date, String orderStatusDesc, String orderFeedback) {
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.servicePrice = servicePrice;
        this.serviceDesc = serviceDesc;
        this.businessName = businessName;
        this.SPContactNo = SPContactNo;
        this.spUid = spUid;
        this.address = address;
        this.spName = spName;
        this.orderId = orderId;
        this.customerUid = customerUid;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.orderStatus = orderStatus;
        this.orderDesc = orderDesc;
        this.date = date;
        this.orderStatusDesc = orderStatusDesc;
        this.orderFeedback = orderFeedback;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getSPContactNo() {
        return SPContactNo;
    }

    public void setSPContactNo(String SPContactNo) {
        this.SPContactNo = SPContactNo;
    }

    public String getSpUid() {
        return spUid;
    }

    public void setSpUid(String spUid) {
        this.spUid = spUid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }
}
