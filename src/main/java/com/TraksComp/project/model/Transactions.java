package com.TraksComp.project.model;


import java.util.UUID;



public class Transactions {



    private final UUID id;
    private double amount;
    private Category category;
    private String description;

    public Transactions(UUID id, double amount, Category category, String description){
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category){
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}
