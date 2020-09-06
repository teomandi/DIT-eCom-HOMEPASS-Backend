package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Availability;
import com.exercise.mybnb.repository.AvailabilityRepo;
import com.exercise.mybnb.repository.PlaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @PostMapping("/places/{pid}/multi-availabilities")//not works.
    public ResponseEntity<?> postMultipleAvailabilitiesForPlace(@PathVariable("pid") int pid,
                                                   Set<Availability> avbs){
        System.out.println("Got availabilities : " + avbs.size());
        return placeRepo.findById(pid).map(place -> {
            System.out.println("found place: " + place.getId() + place.getAddress());
            for (Availability avb: avbs ) {
                avb.setPlace(place);
                availRepo.save(avb);
            }
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

    @DeleteMapping("/places/{pid}/availabilities")
    public ResponseEntity<?> deleteAvailabilitiesFromPlace(@PathVariable("pid") int pid){
        System.out.println("Deleting all availabilities from place " + pid);
        return placeRepo.findById(pid).map(place -> {
            System.out.println("Place found : " + place.getId() + " " + place.getAddress());
            Optional<Set<Availability>> placeAvs = availRepo.findByPlaceId(pid);
            if(placeAvs.isPresent())
                availRepo.deleteAll(placeAvs.get());
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Places " + pid + " not found"));
    }

    @PutMapping("/availabilities/{aid}")
    public Availability editAvailability(@PathVariable("aid") int aid,
                                         Availability avb){
        return availRepo.findById(aid).map(availability -> {
            availability.setFrom(avb.getFrom());
            availability.setTo(avb.getTo());
            return availRepo.save(availability);
        }).orElseThrow(() -> new ResourceNotFoundException("AvailId " + aid + " not found"));
    }

    @DeleteMapping("/availabilities/{aid}")
    public ResponseEntity<?> deleteAvailability(@PathVariable("aid") int aid){
        return availRepo.findById(aid).map(availability -> {
            availRepo.delete(availability);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("AvailId " + aid + " not found"));
    }

    @GetMapping("/datehandling")
    public String datehandler(
            @RequestParam("from") Date from,
            @RequestParam("to") Date to
    ) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date avStart = sdf.parse("2020/8/1");
        Date avEnd = sdf.parse("2020/8/31");

        if(avStart.before(from) && avEnd.after(to)){
            System.out.println("availability accepted");
        }
        return avStart.toString() ;
    }

}
