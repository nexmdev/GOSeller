package com.nexm.goseller.models;

public class Payment {
    private long date;
    private int amount;
    private String description;

    public Payment(){}

    public void setDate(long date) {
        this.date = date;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
