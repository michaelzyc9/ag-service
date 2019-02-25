package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.FloorPlan;
import com.michaelzhang.agservice.projections.FloorPlanProjection;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection= FloorPlanProjection.class)
public interface FloorPlanRepository extends PagingAndSortingRepository<FloorPlan, Long> {
}
