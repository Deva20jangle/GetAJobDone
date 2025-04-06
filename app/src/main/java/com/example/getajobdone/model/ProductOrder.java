package com.example.getajobdone.model;

public class ProductOrder {
    private String orderId;
    private String customerUid;
    private String productId;
    private String address;
    private String productName;
    private String productPrice;
    private String productImage;
    private String spId;
    private String spName;
    private String orderStatus;

    public ProductOrder() {
        // Default constructor required for calls to DataSnapshot.getValue(ProductOrder.class)
    }

    public ProductOrder(String orderId, String customerUid, String productId, String address, String productName, String productPrice, String productImage, String spId, String spName, String orderStatus) {
        this.orderId = orderId;
        this.customerUid = customerUid;
        this.productId = productId;
        this.address = address;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.spId = spId;
        this.spName = spName;
        this.orderStatus = orderStatus;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}