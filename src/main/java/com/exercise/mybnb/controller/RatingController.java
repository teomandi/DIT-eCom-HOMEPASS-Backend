package com.exercise.mybnb.controller;

import com.exercise.mybnb.exception.ResourceNotFoundException;
import com.exercise.mybnb.model.Rating;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.RatingRepo;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {
    @Autowired
    RatingRepo ratingRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PlaceRepo placeRepo;

    @PostMapping("/users/{uid}/places/{pid}/ratings")
    public Rating postRatingForPlace(
            @PathVariable("uid") int uid,
            @PathVariable("pid") int pid,
            Rating rating
    ){
        System.out.println("Post-rating: UserID: " + uid + " PlaceID: " + pid + " Rating " + rating.toString());
        return userRepo.findById(uid).map(user -> {
            System.out.println("User found: " + user.getUsername());
            rating.setUser(user);
            return placeRepo.findById(pid).map(place -> {
                rating.setPlace(place);
                return ratingRepo.save(rating);
            }).orElseThrow(() -> new ResourceNotFoundException("PlaceId " + pid + " not found"));
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + uid + " not found"));
    }

    @DeleteMapping("/ratings/{aid}")
    public ResponseEntity<?> deleteAvailability(@PathVariable("aid") int rid){
        return ratingRepo.findById(rid).map(rating -> {
            ratingRepo.delete(rating);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new org.springframework.data.rest.webmvc.ResourceNotFoundException("RatingId " + rid + " not found"));    }
}
