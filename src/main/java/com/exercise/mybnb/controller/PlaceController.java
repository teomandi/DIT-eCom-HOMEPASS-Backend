package com.exercise.mybnb.controller;

import com.exercise.mybnb.MultiPlace;
import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class PlaceController {

    @Autowired
    PlaceRepo placeRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/place")
    public List<Place> getAllPlaces(){
        return placeRepo.findAll();
    }

    @GetMapping("/place/{pid}")
    public Optional<Place> getPlace(@PathVariable("pid") int pid) {
        return placeRepo.findById(pid);
    }

    @GetMapping("/user/{uid}/place")
    public Page<Place> getAllPlacesByUserId(@PathVariable("uid") int uid, Pageable pageable){
        return placeRepo.findByOwnerId(uid, pageable);
    }


    @PostMapping("/user/{uid}/place")
    public Place createPlace(@PathVariable("uid") int uid, @Valid@RequestBody Place place)
    {
        return userRepo.findById(uid).map(user -> {
            place.setOwner(user);
            return placeRepo.save(place);
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + uid + " not found")) ;
    }

    @PutMapping("/user/{uid}/place/{pid}")
    public Place updatePlace(@PathVariable("uid") int uid,
                             @PathVariable("pid") int pid,
                             @Valid @RequestBody Place p){
        if(!userRepo.existsById(uid))
            throw new ResourceNotFoundException("UserID " + uid + " not found");

        return placeRepo.findById(pid).map(place -> {
            place.setName(p.getName());

            place.setCountry(p.getCountry());
            place.setAddress(p.getCity());
            place.setAddress(p.getAddress());
            place.setaNumber(p.getaNumber());
            place.setZipCode(p.getZipCode());
            place.setDescription(p.getDescription());
            return placeRepo.save(place);
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + "not found"));
    }

    @DeleteMapping("/user/{uid}/place/{pid}")
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
