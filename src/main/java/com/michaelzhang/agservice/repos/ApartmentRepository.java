package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long> {

    List<Apartment> findByAddressIdIn(List<Long> addressIds);
}
