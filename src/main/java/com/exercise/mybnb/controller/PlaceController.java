package com.exercise.mybnb.controller;

import com.exercise.mybnb.exception.ActionNotAllowedException;
import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.model.User;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.UserRepo;
import com.exercise.mybnb.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Place getPlaceByUserId(@PathVariable("uid") int uid){
        System.out.println("loooking for user with id : " + uid);
        //return placeRepo.findById(uid);
        return userRepo.findById(uid).map(User::getPlace).orElseThrow(() -> new ResourceNotFoundException("User Or Place not found"));
    }

    @PostMapping("/users/{uid}/places")
    public Place createPlace(@PathVariable("uid") int uid,
                             @Valid Place place,
                             @RequestParam("image") MultipartFile imageFile )
    {
        System.out.println("Storing new place " + uid);
        return userRepo.findById(uid).map(user -> {
            System.out.println("Owner user found: " + user.getUsername());
            System.out.println("Owner user found: " + user.getPlace());
            //check if he already have
            if( user.getPlace() != null){
                System.out.println("User already have a place");
                throw  new ActionNotAllowedException("User already have a place");
            }
            place.setMainImage(imageFile.getOriginalFilename());
            Place p = placeRepo.save(place);
            user.setPlace(place);
            userRepo.save(user);
            String gallery = p.createGallery();
            if(!imageFile.isEmpty()) {
                try {
                    System.out.println("Storing main image at: " + gallery + imageFile.getOriginalFilename());
                    //main images are not store in the folder
                    Utils.storeImageInGallery(gallery + imageFile.getOriginalFilename(), imageFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return p;
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + uid + " not found")) ;
    }

    @PutMapping("/users/{uid}/places/{pid}")
    public Place updatePlace(@PathVariable("uid") int uid,
                             @PathVariable("pid") int pid,
                             @Valid Place p,
                             @RequestParam("image") MultipartFile imageFile){
        validateUserNplace(uid, pid);
        return placeRepo.findById(pid).map(place -> {
            place.setAddress(p.getAddress());
            place.setLatitude(p.getLatitude());
            place.setLongitude(p.getLongitude());
            place.setMaxGuests(p.getMaxGuests());
            place.setMinCost(p.getMinCost());
            place.setCostPerPerson(p.getCostPerPerson());
            place.setType(p.getType());
            place.setDescription(p.getDescription());
            place.setBeds(p.getBeds());
            place.setBaths(p.getBaths());
            place.setBedrooms(p.getBedrooms());
            place.setLivingRoom(p.isLivingRoom());
            place.setArea(p.getArea());
            if(!imageFile.isEmpty()) {
                try {
                    place.setMainImage(imageFile.getOriginalFilename());
                    String gallery = place.createGallery();
                    System.out.println("Storing main image at: " + gallery + imageFile.getOriginalFilename());
                    //main images are not store in the folder
                    Utils.storeImageInGallery(gallery + imageFile.getOriginalFilename(), imageFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return placeRepo.save(place);
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + "not found"));
    }

    @DeleteMapping("/users/{uid}/places/{pid}")
    public ResponseEntity<?> deletePlace(
            @PathVariable("uid") int uid,
            @PathVariable("pid") int pid
    ){
        validateUserNplace(uid, pid);
        return placeRepo.findByIdAndUserId(pid, uid).map(place -> {
            User owner = userRepo.findById(uid).get();
            owner.setPlace(null);
            userRepo.save(owner);

            placeRepo.delete(place);
            System.out.println("Place " + place.getId() + " was deleted");

            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Place not found with id "
                + pid + " and UserID " + uid));
    }

    public void validateUserNplace(int uid, int pid){
        if(!userRepo.existsById(uid))
            throw new ResourceNotFoundException("UserID " + uid + " not found");
        Optional<User> owner = userRepo.findById(uid);
        if(owner.isPresent())
            if(owner.get().getPlace().getId() != pid)
                throw new ActionNotAllowedException("Place with PlaceID " + pid + " is not of users with UserID " + uid);
    }

}
