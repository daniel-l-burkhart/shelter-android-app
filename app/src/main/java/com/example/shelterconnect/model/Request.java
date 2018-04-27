package com.example.shelterconnect.model;

import java.io.Serializable;

/**
 * Request object to represent model
 * Created by daniel burkhart on 2/12/18.
 */
public class Request implements Serializable {

    private int requestID;

    private int quantity;
    private double amountNeeded;
    private double amountRaised;
    private int employeeID;
    private String name;
    private double itemPrice;
    private int itemID;
    private boolean active;

    public Request(int requestID, int quantity, double amountNeeded, double amountRaised, int employeeID, int itemID, boolean active) {

        if (requestID < 0  || quantity < 0 || amountNeeded < 0 || amountRaised < 0 || employeeID < 0 || itemID < 0) {
            throw new IllegalArgumentException("Invalid input. Try again");
        }

        this.requestID = requestID;
        this.quantity = quantity;
        this.amountNeeded = amountNeeded;
        this.amountRaised = amountRaised;
        this.employeeID = employeeID;
        this.itemID = itemID;
      this.active = active;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmountNeeded() {
        return amountNeeded;
    }

    public void setAmountNeeded(double amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public double getAmountRaised() {
        return amountRaised;
    }

    public void setAmountRaised(double amountRaised) {
        this.amountRaised = amountRaised;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getItemPrice(){
        return this.itemPrice;
    }

    public void setItemPrice(double price){
        this.itemPrice = price;
    }

}
