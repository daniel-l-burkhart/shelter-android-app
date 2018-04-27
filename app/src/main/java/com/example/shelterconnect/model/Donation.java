package com.example.shelterconnect.model;

/**
 * Donation object to represent model
 * Created by daniel on 2/12/18.
 */
public class Donation {

    private int donorID;
    private int requestID;
    private double amountDonated;
    private String donationDate;

    public Donation( int donorID, int requestID, double amountDonated, String donationDate) {
        if ( donorID < 0 || requestID < 0 || amountDonated < 0) {
            throw new IllegalArgumentException("Invalid input. Try again");
        }

        this.donorID = donorID;
        this.requestID = requestID;
        this.amountDonated = amountDonated;
        this.donationDate = donationDate;

    }

    public int getDonorID() {
        return donorID;
    }

    public void setDonorID(int donorID) {
        this.donorID = donorID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public double getAmountDonated() {
        return amountDonated;
    }

    public void setAmountDonated(double amountDonated) {
        this.amountDonated = amountDonated;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }
}
