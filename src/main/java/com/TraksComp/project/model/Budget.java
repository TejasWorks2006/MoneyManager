package com.TraksComp.project.model;

public class Budget {
    private Category category;
    private double max_spending;

    public Budget(Category c, double l){
        this.category = c;
        this.max_spending = l;
    }

    public Category getCategory(){
        return category;
    }
    public void setCategory(Category category){
        this.category = category;
    }
    public double getMax_spending(){
        return max_spending;
    }
    public void setMax_spending(double max_spending){
        this.max_spending = max_spending;
    }
}
