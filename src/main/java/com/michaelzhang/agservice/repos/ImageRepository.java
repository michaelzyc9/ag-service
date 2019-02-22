package com.michaelzhang.agservice.repos;

import com.michaelzhang.agservice.entities.Image;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
}
