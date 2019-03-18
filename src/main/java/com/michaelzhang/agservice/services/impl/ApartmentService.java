package com.michaelzhang.agservice.services.impl;

import com.michaelzhang.agservice.entities.Address;
import com.michaelzhang.agservice.entities.Apartment;
import com.michaelzhang.agservice.entities.FloorPlan;
import com.michaelzhang.agservice.entities.Image;
import com.michaelzhang.agservice.projections.ApartmentProjection;
import com.michaelzhang.agservice.repos.AddressRepository;
import com.michaelzhang.agservice.repos.ApartmentRepository;
import com.michaelzhang.agservice.repos.FloorPlanRepository;
import com.michaelzhang.agservice.repos.ImageRepository;
import com.michaelzhang.agservice.services.IApartmentService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApartmentService implements IApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private FloorPlanRepository floorPlanRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Value("${utils.import.key}")
    private String key;

    @Override
    public List<ApartmentProjection> getApartmentsByFilter(String zipCode, String bedNum, Integer maxPrice, String name) {
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
        return result;
    }

    @Override
    public List<ApartmentProjection> getApartmentsById(Long id) {
        List<Long> idParam = new ArrayList<>();
        idParam.add(id);
        List<ApartmentProjection> result = apartmentRepository.findByIdIn(idParam);
        return result;
    }

    @Override
    public int importDataFromJson(String url, String inputKey) throws IOException, JSONException {
        if (inputKey == null || !inputKey.equals(key))
            return -1;

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
        return dataCount;
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
