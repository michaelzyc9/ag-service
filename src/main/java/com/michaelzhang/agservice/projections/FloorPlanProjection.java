package com.michaelzhang.agservice.projections;

import com.michaelzhang.agservice.entities.FloorPlan;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {FloorPlan.class})
public interface FloorPlanProjection {

    float getBed();

    float getBath();

    int getPriceFrom();

}