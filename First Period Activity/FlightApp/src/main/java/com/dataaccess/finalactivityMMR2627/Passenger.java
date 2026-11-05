package com.dataaccess.finalactivityMMR2627;

public class Passenger {
    private String passsportNo;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public Passenger(){
    }

    public Passenger(String passsportNo, String firstName, String lastName, String address, String phone) {
        this.passsportNo = passsportNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }

    public String getPasssportNo() {
        return passsportNo;
    }

    public void setPasssportNo(String passsportNo) {
        this.passsportNo = passsportNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
