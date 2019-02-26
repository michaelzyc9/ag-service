package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.FloorPlan;
import com.michaelzhang.agservice.projections.FloorPlanProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection= FloorPlanProjection.class)
public interface FloorPlanRepository extends PagingAndSortingRepository<FloorPlan, Long> {

    @Override
    @RestResource(exported = false)
    Iterable<FloorPlan> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    Page<FloorPlan> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends FloorPlan> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends FloorPlan> Iterable<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    FloorPlan findOne(Long aLong);

    @Override
    boolean exists(Long aLong);

    @Override
    @RestResource(exported = false)
    Iterable<FloorPlan> findAll();

    @Override
    @RestResource(exported = false)
    Iterable<FloorPlan> findAll(Iterable<Long> iterable);

    @Override
    long count();

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(FloorPlan floorPlan);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends FloorPlan> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
