package com.michaelzhang.agservice.entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Apartment extends AbstractEntity {

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Address address;
    private String website;
    @OneToMany(mappedBy = "apartment")
    private Set<FloorPlan> floorPlans;
    @OneToMany(mappedBy = "apartment")
    private Set<Image> images;

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

    public Set<FloorPlan> getFloorPlans() {
        return floorPlans;
    }

    public void setFloorPlans(Set<FloorPlan> floorPlans) {
        this.floorPlans = floorPlans;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

}
