package com.michaelzhang.agservice.projections;


import com.michaelzhang.agservice.entities.Address;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Address.class})
public interface AddressProjection {

    String getFullAddress();

}
