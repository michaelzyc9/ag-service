package com.michaelzhang.agservice.projections;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.entities.FloorPlan;
import com.michaelzhang.agservice.entities.Image;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "default", types = {Apartment.class})
@JsonPropertyOrder({"name","website","address"})
public interface ApartmentProjection {

    String getName();

    Address getAddress();

    String getWebsite();

    Set<FloorPlan> getFloorPlans();

    Set<Image> getImages();

}
