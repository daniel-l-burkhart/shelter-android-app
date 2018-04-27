package com.example.shelterconnect.model;

/**
 * Request object to represent model
 * Created by daniel burkhart on 2/12/18.
 */
public class Requestnew {

    private int requestID;
    private int quantity;
    private double amountNeeded;
    private double amountRaised;
    private int workerID;
    private int itemID;
    private boolean edited;
    private boolean active;

    public Requestnew(int requestID, int quantity, double amountNeeded, double amountRaised, int workerID, int itemID, boolean active) {

        if (requestID < 0 || amountNeeded < 0 || amountRaised < 0 || workerID < 0  || quantity < 0 ||itemID < 0 ) {
            throw new IllegalArgumentException("Invalid input. Try again");
        }

        this.requestID = requestID;
        this.quantity = quantity;
        this.amountNeeded = amountNeeded;
        this.amountRaised = amountRaised;
        this.workerID = workerID;
        this.itemID = itemID;
        this.active = active;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
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
        return workerID;
    }

    public void setEmployeeID(int employeeID) {
        this.workerID = workerID;
    }

    public int getItemID() {return itemID;}

    public void setItemID(int itemID) {this.itemID = itemID;}

      public boolean isActive() {return active;}
      public void setActive(boolean active) {this.active = active;}

    public boolean hasBeenEdited(){
        return this.edited;
    }

    public void setEdited(boolean newVal){
        this.edited = newVal;
    }

}
