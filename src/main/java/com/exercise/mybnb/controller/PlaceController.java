package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.UserRepo;
import com.exercise.mybnb.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class PlaceController {

    @Autowired
    PlaceRepo placeRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/places")
    public List<Place> getAllPlaces(){
        return placeRepo.findAll();
    }
//    public Page<Place> getAllPlaces(Pageable pageable){
//        return placeRepo.findAll(pageable);
//    }


    @GetMapping("/places/{pid}")
    public Optional<Place> getPlace(@PathVariable("pid") int pid) {
        return placeRepo.findById(pid);
    }

    @GetMapping("/users/{uid}/places")
    public Page<Place> getAllPlacesByUserId(@PathVariable("uid") int uid, Pageable pageable){
        return placeRepo.findByOwnerId(uid, pageable);
    }


    @PostMapping("/users/{uid}/places")
    public Place createPlace(@PathVariable("uid") int uid, @Valid Place place, @RequestParam("image") MultipartFile imageFile )
    {
        return userRepo.findById(uid).map(user -> {
            place.setOwner(user);
            place.setMainImage(imageFile.getOriginalFilename());
            if(imageFile != null) {
                try {
                    //main images are not store in the folder
                    Utils.storeImage("places/" + imageFile.getOriginalFilename(), imageFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return placeRepo.save(place);
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + uid + " not found")) ;
    }
//
//    @PutMapping("/users/{uid}/places/{pid}")
//    public Place updatePlace(@PathVariable("uid") int uid,
//                             @PathVariable("pid") int pid,
//                             @Valid @RequestBody Place p){
//        if(!userRepo.existsById(uid))
//            throw new ResourceNotFoundException("UserID " + uid + " not found");
//
//        return placeRepo.findById(pid).map(place -> {
//            place.setName(p.getName());
//
//            place.setCountry(p.getCountry());
//            place.setAddress(p.getCity());
//            place.setAddress(p.getAddress());
//            place.setaNumber(p.getaNumber());
//            place.setZipCode(p.getZipCode());
//            place.setDescription(p.getDescription());
//            return placeRepo.save(place);
//        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + "not found"));
//    }

    @DeleteMapping("/users/{uid}/places/{pid}")
    public ResponseEntity<?> deletePlace(@PathVariable("uid") int uid,
                              @PathVariable("pid") int pid){
        return placeRepo.findByIdAndOwnerId(pid, uid).map(place -> {
            placeRepo.delete(place);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Place not found with id "
                + pid + " and UserID " + uid));
    }

//    @PostMapping("/storePlaceImage")//int placeId, MultipartFile imageFile  //maybe replace and delete multiplace
//    public String storeImage(MultiPlace mp){
//        mp.storeImage();
//        if(!placeRepo.existsById(mp.getPlaceId()))
//            return "{'error':'place not found'}";
//        Optional<Place> p = placeRepo.findById(mp.getPlaceId());
//        p.get().addImage(mp.getImageFile().getOriginalFilename());
//        placeRepo.save(p.get());
//        return "{ 'success' : 'place image added'}";
//    }
}
