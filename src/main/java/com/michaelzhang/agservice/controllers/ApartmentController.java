package com.michaelzhang.agservice.controllers;

import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.entities.FloorPlan;
import com.michaelzhang.agservice.projections.ApartmentProjection;
import com.michaelzhang.agservice.services.IApartmentService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RepositoryRestController
@RequestMapping("/apartments")
public class ApartmentController {

    @Autowired
    private IApartmentService apartmentService;

    @GetMapping("/search")
    public @ResponseBody
    ResponseEntity getApartments(@RequestParam(value = "zipCode", required = false) String zipCode,
                                 @RequestParam(value = "bed", required = false) String bedNum,
                                 @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                 @RequestParam(value = "name", required = false) String name) {

        List<ApartmentProjection> result = apartmentService.getApartmentsByFilter(zipCode, bedNum, maxPrice, name);
        Resources<Resource<ApartmentProjection>> resources = new Resources(result);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity getApartmentById(@PathVariable("id") Long id) {
        List<ApartmentProjection> result = apartmentService.getApartmentsById(id);
        Resources<Resource<ApartmentProjection>> resources = new Resources(result);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/import")
    @RestResource(exported = false)
    public ResponseEntity importData(@RequestParam("url") String url,
                                     @RequestParam("key") String inputKey) throws IOException, JSONException {
        int dataCount = apartmentService.importDataFromJson(url,inputKey);
        if(dataCount == -1){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("INCORRECT KEY");
        }
        return ResponseEntity.ok(dataCount + " Apartment records created");

    }
}
