package com.michaelzhang.agservice.controllers;

import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.entities.FloorPlan;
import com.michaelzhang.agservice.projections.ApartmentProjection;
import com.michaelzhang.agservice.repos.AddressRepository;
import com.michaelzhang.agservice.repos.ApartmentRepository;
import com.michaelzhang.agservice.repos.FloorPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RepositoryRestController
@RequestMapping("/apartments")
public class ApartmentController {
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private FloorPlanRepository floorPlanRepository;

    @GetMapping("/search")
    public @ResponseBody
    ResponseEntity getApartments(@RequestParam(value = "zipCode", required = false) String zipCode,
                                 @RequestParam(value = "bed", required = false) String bedNum,
                                 @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                 @RequestParam(value = "name", required = false) String name) {

        List<ApartmentProjection> result = null;

        List<ApartmentProjection> apartmentsByZipCode;
        if (zipCode != null && !zipCode.isEmpty()) {
            apartmentsByZipCode = getApartmentsByZipCode(zipCode);
            result = apartmentsByZipCode;
        }

        List<ApartmentProjection> apartmentsByBedNum;
        if (bedNum != null && !bedNum.isEmpty()) {
            apartmentsByBedNum = getApartmentsByBedNum(bedNum);
            if (result == null)
                result = apartmentsByBedNum;
            else
                result = getIntersection(result, apartmentsByBedNum);
        }

        List<ApartmentProjection> apartmentsByMaxPrice;
        if (maxPrice != null) {
            apartmentsByMaxPrice = getApartmentsByMaxPrice(maxPrice);
            if (result == null)
                result = apartmentsByMaxPrice;
            else
                result = getIntersection(result, apartmentsByMaxPrice);
        }

        List<ApartmentProjection> apartmentsByName;
        if (name != null && !name.isEmpty()) {
            apartmentsByName = getApartmentsByName(name);
            if (result == null)
                result = apartmentsByName;
            else
                result = getIntersection(result, apartmentsByName);
        }

        Resources<Resource<ApartmentProjection>> resources = new Resources(result);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity getApartmentById(@PathVariable("id") Long id) {
        List<Long> idParam = new ArrayList<>();
        idParam.add(id);
        List<ApartmentProjection> result = apartmentRepository.findByIdIn(idParam);
        Resources<Resource<ApartmentProjection>> resources = new Resources(result);
        return ResponseEntity.ok(resources);
    }

    private List<ApartmentProjection> getApartmentsByZipCode(String zipCode) {
        if (zipCode == null || zipCode.isEmpty())
            return null;

        List<Address> addresses = addressRepository.findByZipCode(zipCode);
        List<Long> addressIds = new ArrayList<>();
        for (Address address : addresses) {
            addressIds.add(address.getId());
        }
        List<ApartmentProjection> apartments = apartmentRepository.findByAddressIdIn(addressIds);
        return apartments;
    }

    private List<ApartmentProjection> getApartmentsByName(String name) {
        return apartmentRepository.findByNameIgnoreCaseContaining(name);
    }

    private List<ApartmentProjection> getApartmentsByMaxPrice(Integer maxPrice) {
        List<FloorPlan> fpList = floorPlanRepository.findByPriceFromLessThan(maxPrice);
        Set<Long> aptIdSet = new HashSet();
        for (FloorPlan fp : fpList) {
            aptIdSet.add(fp.getApartment().getId());
        }
        List<Long> idList = new ArrayList<>(aptIdSet);
        List<ApartmentProjection> results = apartmentRepository.findByIdIn(idList);
        return results;
    }

    private List<ApartmentProjection> getApartmentsByBedNum(String bedNum) {
        List<FloorPlan> fpList = floorPlanRepository.findByBed(Float.valueOf(bedNum));
        Set<Long> aptIdSet = new HashSet();
        for (FloorPlan fp : fpList) {
            aptIdSet.add(fp.getApartment().getId());
        }
        List<Long> idList = new ArrayList<>(aptIdSet);
        List<ApartmentProjection> results = apartmentRepository.findByIdIn(idList);
        return results;
    }

    private List<ApartmentProjection> getIntersection(List<ApartmentProjection> l1, List<ApartmentProjection> l2) {
        return null;
    }


}
