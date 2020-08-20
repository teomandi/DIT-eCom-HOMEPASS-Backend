package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Availability;
import com.exercise.mybnb.repository.AvailabilityRepo;
import com.exercise.mybnb.repository.PlaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class AvailabilityController {
    @Autowired
    AvailabilityRepo availRepo;
    @Autowired
    PlaceRepo placeRepo;

    @GetMapping("/availabilities")
    public List<Availability> getAllAvailabilities(){
        return availRepo.findAll();
    }

    @GetMapping("/places/{pid}/availabilities")
    public Optional<Set<Availability>> getAvailabilitiesByPlace(@PathVariable("pid") int pid){
        return availRepo.findByPlaceId(pid);
    }
    @PostMapping("/places/{pid}/availabilities")
    public Availability postAvailabilitiesForPlace(@PathVariable("pid") int pid,
                                                     Availability avb){
        System.out.println("Got availability : " + avb.toString());
        return placeRepo.findById(pid).map(place -> {
            System.out.println("found place: " + place.getId() + place.getAddress());
            avb.setPlace(place);
            return availRepo.save(avb);
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

}
