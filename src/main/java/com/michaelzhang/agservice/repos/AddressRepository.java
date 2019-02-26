package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.projections.AddressProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection= AddressProjection.class)
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {

    @RestResource(exported = false)
    List<Address> findByZipCode(String zipCode);

    @Override
    @RestResource(exported = false)
    Iterable<Address> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    Page<Address> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends Address> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Address> Iterable<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    Address findOne(Long aLong);

    @Override
    boolean exists(Long aLong);

    @Override
    @RestResource(exported = false)
    Iterable<Address> findAll();

    @Override
    @RestResource(exported = false)
    Iterable<Address> findAll(Iterable<Long> iterable);

    @Override
    long count();

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(Address address);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Address> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
