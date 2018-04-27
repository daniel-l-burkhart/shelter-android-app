package com.example.shelterconnect.model;

/**
 * Employee object to represent model
 * Created by daniel on 2/12/18.
 */
public class Employee {

    private int employeeID;
    private String name;
    private int position;
    private String email;
    private String phone;
    private String address;

    public Employee(int employeeID, String name, int position, String phone, String email, String address) {
        if (employeeID < 0 || name == null || position < 1 || email == null || address == null) {
            throw new IllegalArgumentException("Invalid input");
        }
        this.employeeID = employeeID;
        this.name = name;
        this.position = position;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
