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

        List<Long> aptIds = null;

        List<Long> aptIdsByZipCode;
        if (zipCode != null && !zipCode.isEmpty()) {
            aptIdsByZipCode = getAptIdByZipCode(zipCode);
            aptIds = aptIdsByZipCode;
        }

        List<Long> aptIdsByBedNum;
        if (bedNum != null && !bedNum.isEmpty()) {
            aptIdsByBedNum = getAptIdsByBedNum(bedNum);
            if (aptIds == null)
                aptIds = aptIdsByBedNum;
            else
                aptIds = getIntersection(aptIds, aptIdsByBedNum);
        }

        List<Long> aptIdsByMaxPrice;
        if (maxPrice != null) {
            aptIdsByMaxPrice = getAptIdsByMaxPrice(maxPrice);
            if (aptIds == null)
                aptIds = aptIdsByMaxPrice;
            else
                aptIds = getIntersection(aptIds, aptIdsByMaxPrice);
        }

        List<Long> aptIdsByName;
        if (name != null && !name.isEmpty()) {
            aptIdsByName = getAptIdsByName(name);
            if (aptIds == null)
                aptIds = aptIdsByName;
            else
                aptIds = getIntersection(aptIds, aptIdsByName);
        }

        List<ApartmentProjection> result = apartmentRepository.findByIdIn(aptIds);
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

    private List<Long> getAptIdByZipCode(String zipCode) {
        List<Address> addresses = addressRepository.findByZipCode(zipCode);
        List<Long> addressIds = new ArrayList<>();
        for (Address address : addresses) {
            addressIds.add(address.getId());
        }
        List<Apartment> apartments = apartmentRepository.findByAddressIdIn(addressIds);
        List<Long> aptIds = new ArrayList<>();
        for (Apartment apt : apartments) {
            aptIds.add(apt.getId());
        }
        return aptIds;
    }

    private List<Long> getAptIdsByName(String name) {
        List<Apartment> apartments = apartmentRepository.findByNameIgnoreCaseContaining(name);
        List<Long> aptIds = new ArrayList<>();
        for (Apartment apt : apartments) {
            aptIds.add(apt.getId());
        }
        return aptIds;
    }

    private List<Long> getAptIdsByMaxPrice(Integer maxPrice) {
        List<FloorPlan> fpList = floorPlanRepository.findByPriceFromLessThan(maxPrice);
        Set<Long> aptIdSet = new HashSet();
        for (FloorPlan fp : fpList) {
            aptIdSet.add(fp.getApartment().getId());
        }
        List<Long> aptIds = new ArrayList<>(aptIdSet);
        return aptIds;
    }

    private List<Long> getAptIdsByBedNum(String bedNum) {
        List<FloorPlan> fpList = floorPlanRepository.findByBed(Float.valueOf(bedNum));
        Set<Long> aptIdSet = new HashSet();
        for (FloorPlan fp : fpList) {
            aptIdSet.add(fp.getApartment().getId());
        }
        List<Long> aptIds = new ArrayList<>(aptIdSet);
        return aptIds;
    }

    private List<Long> getIntersection(List<Long> l1, List<Long> l2) {
        l1.retainAll(l2);
        return l1;
    }


}
