package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.projections.AddressProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection= AddressProjection.class)
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {

    List<Address> findByZipCode(String zipCode);

}
