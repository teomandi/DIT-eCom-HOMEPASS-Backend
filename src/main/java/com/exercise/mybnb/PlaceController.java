package com.exercise.mybnb;

import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PlaceController {

    @Autowired
    PlaceRepo placeRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/place")
    public List<Place> getPlaces(){
        return placeRepo.findAll();
    }

    @GetMapping("/place/{id}")
    public Optional<Place> getPlace(@PathVariable("id") int pid) {
        return placeRepo.findById(pid);
    }

    @PostMapping("/place")
    public Place addPlace(Place p){
        placeRepo.save(p);
        return p;
    }

    @PutMapping("/place")
    public Place updatePlace(Place p){
        placeRepo.save(p);
        return p;
    }

    @DeleteMapping("/place/{id}")
    public String deletePlace(@PathVariable("id") int pid){
        Place p = placeRepo.getOne(pid);
        placeRepo.delete(p);;
        return "{ 'message' : 'place deleted with success'}";
    }
}
