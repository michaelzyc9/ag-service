package com.michaelzhang.agservice.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class FloorPlan extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Apartment apartment;
    private float bed;
    private float bath;
    private int priceFrom;

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public float getBed() {
        return bed;
    }

    public void setBed(float bed) {
        this.bed = bed;
    }

    public float getBath() {
        return bath;
    }

    public void setBath(float bath) {
        this.bath = bath;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(id, ((FloorPlan) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
