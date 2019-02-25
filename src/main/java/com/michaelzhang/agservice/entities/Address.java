package com.michaelzhang.agservice.entities;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Address extends AbstractEntity {

    private String streetNumber;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getFullAddress(){
        String fullAddress = streetNumber + " " +
                            street + ", " +
                            city + ", " +
                            state + " " +
                            zipCode;
        return fullAddress;
    }

}
