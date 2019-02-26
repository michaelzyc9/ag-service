package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Image;
import com.michaelzhang.agservice.projections.ImageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection= ImageProjection.class)
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

    @Override
    @RestResource(exported = false)
    Iterable<Image> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    Page<Image> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends Image> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Image> Iterable<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    Image findOne(Long aLong);

    @Override
    boolean exists(Long aLong);

    @Override
    @RestResource(exported = false)
    Iterable<Image> findAll();

    @Override
    @RestResource(exported = false)
    Iterable<Image> findAll(Iterable<Long> iterable);

    @Override
    long count();

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(Image image);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Image> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
