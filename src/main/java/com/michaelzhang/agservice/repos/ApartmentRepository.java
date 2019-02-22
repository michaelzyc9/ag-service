package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Apartment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long> {
}
