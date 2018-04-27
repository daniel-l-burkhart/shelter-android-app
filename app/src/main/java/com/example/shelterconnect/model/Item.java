package com.example.shelterconnect.model;

/**
 * Item object for model
 * Created by daniel on 2/12/18.
 */
public class Item {

    private int itemID;
    private String name;
    private double price;
    private int quantity;
    private boolean edited;

    public Item(int itemID, String name, double price, int quantity) {
        if (itemID < 0 || name == null || price < 0.0 || quantity < 0) {
            throw new IllegalArgumentException("Invalid input");
        }

        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.edited = false;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean hasBeenEdited(){
        return this.edited;
    }

    public void setEdited(boolean newVal){
        this.edited = newVal;
    }
}
