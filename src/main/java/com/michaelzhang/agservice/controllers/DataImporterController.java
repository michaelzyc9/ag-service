package com.michaelzhang.agservice.controllers;

import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.entities.FloorPlan;
import com.michaelzhang.agservice.entities.Image;
import com.michaelzhang.agservice.repos.AddressRepository;
import com.michaelzhang.agservice.repos.ApartmentRepository;
import com.michaelzhang.agservice.repos.FloorPlanRepository;
import com.michaelzhang.agservice.repos.ImageRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

@RepositoryRestController
@RequestMapping("/apartments")
public class DataImporterController {

    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private FloorPlanRepository floorPlanRepository;

    @PostMapping("/import")
    public ResponseEntity importData(@RequestParam("url") String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        int dataCount = 0;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray jsonArray = new JSONArray(jsonText);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Address address = importAddress(obj.getString("address"));

                Apartment apt = new Apartment();
                apt.setName(obj.getString("name"));
                apt.setWebsite(obj.getString("website"));
                apt.setAddress(address);

                Set<FloorPlan> floorPlans = importFloorPlan(obj.getJSONArray("floor_plans"), apt);
                Set<Image> images = importImage(obj.getJSONArray("images"), apt);

                addressRepository.save(address);
                apartmentRepository.save(apt);
                floorPlanRepository.save(floorPlans);
                imageRepository.save(images);
                dataCount++;
            }

        } catch (JSONException e) {
            throw new JSONException("Import Failed! JSONException: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Import Failed! IOException: " + e.getMessage());
        } finally {
            is.close();
        }

        return ResponseEntity.ok(dataCount + " Apartment records created");

    }

    private Address importAddress(String address) {
        String[] addrSplit = address.split(",");
        if (addrSplit.length != 3) {
            throw new JSONException("Unexpected Address - " + address);
        }
        String streetNum = addrSplit[0].trim().split(" ")[0].trim();
        String street = addrSplit[0].trim().replace(streetNum, "").trim();
        String city = addrSplit[1].trim();
        String state = addrSplit[2].trim().split(" ")[0].trim();
        String zipCode = addrSplit[2].trim().replace(state, "").trim();

        Address newAddress = new Address();
        newAddress.setStreetNumber(streetNum);
        newAddress.setStreet(street);
        newAddress.setCity(city);
        newAddress.setState(state);
        newAddress.setZipCode(zipCode);

        return newAddress;
    }

    private Set<Image> importImage(JSONArray imageJSONArr, Apartment apt) {
        Set<Image> images = new HashSet<>();
        for (int i = 0; i < imageJSONArr.length(); i++) {
            String description;
            if (i == 0)
                description = "Exterior";
            else if (i == 1)
                description = "Amenity";
            else
                description = "Interior";

            String link = imageJSONArr.getString(i);
            Image newImage = new Image();
            newImage.setLink(link);
            newImage.setDescription(description);
            newImage.setApartment(apt);

            images.add(newImage);
        }
        return images;
    }

    private Set<FloorPlan> importFloorPlan(JSONArray fpJSONArr, Apartment apt) {
        Set<FloorPlan> floorPlans = new HashSet<>();
        for (int i = 0; i < fpJSONArr.length(); i++) {
            JSONObject fpJSONObj = fpJSONArr.getJSONObject(i);
            FloorPlan newFloorPlan = new FloorPlan();
            newFloorPlan.setBed(Float.valueOf(fpJSONObj.getString("bed")));
            newFloorPlan.setBath(Float.valueOf(fpJSONObj.getString("bath")));
            newFloorPlan.setPriceFrom(Integer.valueOf(fpJSONObj.getString("price_from")));
            newFloorPlan.setApartment(apt);

            floorPlans.add(newFloorPlan);
        }
        return floorPlans;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
