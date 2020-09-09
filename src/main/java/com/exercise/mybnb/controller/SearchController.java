package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.model.Search;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.SearchRepo;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    PlaceRepo placeRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    SearchRepo searchRepo;

    @GetMapping("/search/{uid}/has")
    public boolean hasSearched(@PathVariable("uid") int uid){
        return userRepo.findById(uid).map(user -> {
            return searchRepo.existsByUser(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User " + uid + " not found"));
    }

    @GetMapping("/search/places")
    public List<Place> getPreviousSearches(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam("uid") int uid
    ){
        return userRepo.findById(uid).map(user -> {
            System.out.println("user for search history found: " + user.getUsername());
            List<Search> lastSearches = searchRepo.findByUser(user);
            List<Place> places = new ArrayList<>();
            for(Search search: lastSearches){
                double minLat = search.getLatitude() - 0.3f;
                double maxLat = search.getLatitude() + 0.3f;
                double minLong = search.getLongitude() - 0.3f;
                double maxLong = search.getLongitude() + 0.3f;
                places.addAll(placeRepo.getClosePlaces(minLat, maxLat, minLong, maxLong));
            }
            int startIndex = pageNo * pageSize;
            int endIndex = (pageNo * pageSize) + pageSize -1;
            System.out.println("Start: " + startIndex + " End: " + endIndex);
            System.out.println("found searched results ::" + places.size());
            if(places.size() == 0)
                return places;
            if(places.size() >= startIndex && places.size() > endIndex) {
                System.out.println("Case 1");
                return places.subList(startIndex, endIndex);
            }
            else if(places.size() >= startIndex && places.size() < endIndex) {
                System.out.println("Case 2");
                return places.subList(startIndex, places.size());
            }
            else {
                System.out.println("Case 3");
                return null;
            }
        }).orElseThrow(() -> new ResourceNotFoundException("User " + uid + " not found"));
    }
}
