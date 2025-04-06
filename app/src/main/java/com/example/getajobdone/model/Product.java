package com.example.getajobdone.model;

public class Product {
    private String productId;
    private String productName;
    private String productPrice;
    private String productImage;
    private String spId;
    private String spName;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String productId, String productName, String productPrice, String productImage, String spId, String spName) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.spId = spId;
        this.spName = spName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
}