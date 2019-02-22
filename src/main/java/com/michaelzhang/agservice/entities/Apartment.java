package com.michaelzhang.agservice.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class Apartment extends AbstractEntity {

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Address address;
    private String website;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(id, ((Apartment) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
