package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Address;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
}
