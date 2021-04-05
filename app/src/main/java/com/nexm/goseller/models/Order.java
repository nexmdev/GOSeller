package com.nexm.goseller.models;

public class Order {
    private String productName;
    private String sellertName;
    private String sellerId;
    private String productId;
    private String price;
    private int quantity;
    private long date;
    private int deliveryCharges;
    private String status = "ORDERED";
    private String productThumb;
    private String customerID;
    private String deliveryAddressID;
    private String deliveryAgent;
    private String customerNameAddress;

    public Order(){}

    public void setCustomerNameAddress(String customerNameAddress) {
        this.customerNameAddress = customerNameAddress;
    }

    public String getCustomerNameAddress() {
        return customerNameAddress;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setDeliveryCharges(int deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductThumb(String productThumb) {
        this.productThumb = productThumb;
    }

    public void setSellertName(String sellertName) {
        this.sellertName = sellertName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellertName() {
        return sellertName;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDeliveryCharges() {
        return deliveryCharges;
    }

    public String getPrice() {
        return price;
    }

    public String getProductThumb() {
        return productThumb;
    }

    public long getDate() {
        return date;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public String getDeliveryAgent() {
        return deliveryAgent;
    }

    public String getStatus() {
        return status;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDeliveryAddressID(String deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public void setDeliveryAgent(String deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
