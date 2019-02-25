package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.projections.ApartmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection= ApartmentProjection.class)
public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long> {

    List<ApartmentProjection> findByAddressIdIn(List<Long> addressIds);

}
