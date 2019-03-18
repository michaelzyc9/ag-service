package com.michaelzhang.agservice.services;

import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.projections.ApartmentProjection;

import java.io.IOException;
import java.util.List;

public interface IApartmentService {

    List<ApartmentProjection> getApartmentsByFilter(String zipCode, String bedNum, Integer maxPrice, String name);

    List<ApartmentProjection> getApartmentsById(Long id);

    int importDataFromJson(String url, String inputKey) throws IOException;

}
