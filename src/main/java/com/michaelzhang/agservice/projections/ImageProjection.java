package com.michaelzhang.agservice.projections;

import com.michaelzhang.agservice.entities.Image;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Image.class})
public interface ImageProjection {

    String getLink();

    String getDescription();

}