package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {

    List<Address> findByZipCode(String zipCode);

}
