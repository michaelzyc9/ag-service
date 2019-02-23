package com.michaelzhang.agservice.controllers;

import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.repos.AddressRepository;
import com.michaelzhang.agservice.repos.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RepositoryRestController
@RequestMapping("/apartments")
public class ApartmentController {
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("/search")
    public @ResponseBody ResponseEntity getApartmentsByZipCode(@RequestParam("zipCode") String zipCode) {
        List<Address> addresses = addressRepository.findByZipCode(zipCode);
        List<Long> addressIds = new ArrayList<>();
        for (Address address : addresses) {
            addressIds.add(address.getId());
        }
        List<Apartment> apartments = apartmentRepository.findByAddressIdIn(addressIds);
        List<Resource> resourceList = new ArrayList<>();
        for(Apartment apt : apartments){
            Resource<Apartment> newResource = new Resource<>(apt);

            newResource.add(linkTo(methodOn(ApartmentController.class).getApartmentsByZipCode(zipCode)).withSelfRel());
            newResource.add(linkTo(ApartmentController.class).slash(apt.getId()).slash("address").withRel("address"));
            newResource.add(linkTo(ApartmentController.class).slash(apt.getId()).slash("images").withRel("images"));
            newResource.add(linkTo(ApartmentController.class).slash(apt.getId()).slash("floorPlans").withRel("floorPlans"));

            resourceList.add(newResource);
        }
        Resources<Resource<Apartment>> resources = new Resources(resourceList);
        return ResponseEntity.ok(resources);
    }
}
