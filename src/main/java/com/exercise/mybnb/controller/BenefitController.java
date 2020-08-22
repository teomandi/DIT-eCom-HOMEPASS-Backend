package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Benefit;
import com.exercise.mybnb.repository.BenefitRepo;
import com.exercise.mybnb.repository.PlaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class BenefitController {
    @Autowired
    BenefitRepo benefitRepo;
    @Autowired
    PlaceRepo placeRepo;

    @GetMapping("/benefits")
    public List<Benefit> getAllBenefits(){
        return benefitRepo.findAll();
    }

    @GetMapping("/places/{pid}/benefits")
    public Optional<Set<Benefit>> getBenefitsByPlace(@PathVariable("pid") int pid){
        return benefitRepo.findByPlaceId(pid);
    }
    @PostMapping("/places/{pid}/benefits")
    public Benefit postBenefitForPlace(@PathVariable("pid") int pid,
                                 Benefit benefit){
        System.out.println("Got benefit : " + benefit.getContent());
        return placeRepo.findById(pid).map(place -> {
            System.out.println("found place: " + place.getId() + place.getAddress());
            benefit.setPlace(place);
            return benefitRepo.save(benefit);
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

    @PostMapping("/places/{pid}/multi-benefits")//not works.
    public ResponseEntity<?> postMultipleBenefitsForPlace(@PathVariable("pid") int pid,
                                                       List<Benefit> benefits){
        System.out.println("Got benefit : " + benefits.size());
        return placeRepo.findById(pid).map(place -> {
            System.out.println("found place: " + place.getId() + place.getAddress());
            for (Benefit benefit: benefits ) {
                benefit.setPlace(place);
                benefitRepo.save(benefit);
            }
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

    @DeleteMapping("/places/{pid}/benefits")
    public ResponseEntity<?> deleteBenefitsFromPlace(@PathVariable("pid") int pid){
        System.out.println("Deleting all benefits from place " + pid);
        return placeRepo.findById(pid).map(place -> {
            System.out.println("Place found : " + place.getId() + " " + place.getAddress());
            Optional<Set<Benefit>> placeBenefits = benefitRepo.findByPlaceId(pid);
            if(placeBenefits.isPresent())
                benefitRepo.deleteAll(placeBenefits.get());
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Places " + pid + " not found"));
    }

    @PutMapping("/benefits/{bid}")
    public Benefit editBenefit(@PathVariable("bid") int bid,
                         Benefit b){
        return benefitRepo.findById(bid).map(benefit -> {
            benefit.setContent(b.getContent());
            return benefitRepo.save(benefit);
        }).orElseThrow(() -> new ResourceNotFoundException("BenefitId " + bid + " not found"));
    }

    @DeleteMapping("/benefits/{bid}")
    public ResponseEntity<?> deleteBenefit(@PathVariable("bid") int bid){
        return benefitRepo.findById(bid).map(benefit -> {
            benefitRepo.delete(benefit);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("BenefitId " + bid + " not found"));
    }
}
