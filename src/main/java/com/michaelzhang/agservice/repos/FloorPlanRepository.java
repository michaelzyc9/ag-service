package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.FloorPlan;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FloorPlanRepository extends PagingAndSortingRepository<FloorPlan, Long> {
}
