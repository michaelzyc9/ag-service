package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Image;
import com.michaelzhang.agservice.projections.ImageProjection;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection= ImageProjection.class)
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
}
