package com.exercise.mybnb.controller;

import com.exercise.mybnb.exception.ActionNotAllowedException;
import com.exercise.mybnb.model.Availability;
import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.model.Search;
import com.exercise.mybnb.model.User;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.SearchRepo;
import com.exercise.mybnb.repository.UserRepo;
import com.exercise.mybnb.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class PlaceController {

    @Autowired
    PlaceRepo placeRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    SearchRepo searchRepo;

    @GetMapping("/places")
    public ResponseEntity<List<Place>> getAllPlaces(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "costPerPerson") String sortBy
    ){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Place> pagedResult = placeRepo.findAll(paging);
        List<Place> placeList = null;
        if(pagedResult.hasContent()) {
            placeList = pagedResult.getContent();
        } else {
            placeList = new ArrayList<Place>();
        }
        System.out.println("All places that found: " + placeList.size());
        return new ResponseEntity<List<Place>>(placeList, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/places/{pid}")
    public Optional<Place> getPlace(@PathVariable("pid") int pid) {
        return placeRepo.findById(pid);
    }

    @GetMapping("/users/{uid}/places")
    public Place getPlaceByUserId(@PathVariable("uid") int uid){
        System.out.println("looking for user with id : " + uid);
        //return placeRepo.findById(uid);
        return userRepo.findById(uid).map(User::getPlace).orElseThrow(() -> new ResourceNotFoundException("User Or Place not found"));
    }

    @GetMapping("/users/username/{username}/places")
    public Place getPlaceByUsername(@PathVariable("username") String username){
        System.out.println("loooking for user with username : " + username);
        return userRepo.findByUsername(username).getPlace();
    }

    @PostMapping("/users/{uid}/places")
    public Place createPlace(@PathVariable("uid") int uid,
                             @Valid Place place,
                             @RequestParam("image") MultipartFile imageFile )
    {
        System.out.println("Storing new place " + uid);
        return userRepo.findById(uid).map(user -> {
            System.out.println("Owner user found: " + user.getUsername());
            System.out.println("his place: " + user.getPlace());
            //check if he already have
            if( user.getPlace() != null){
                System.out.println("User already have a place");
                throw  new ActionNotAllowedException("User already have a place");
            }
//            RandomStringUtils.random(8, true, true)
            String imageExt = FilenameUtils.getExtension(imageFile.getOriginalFilename());
            System.out.println("Main image extension is: " + imageExt);
            String imageName = "main." + imageExt;
            place.setMainImage(imageName);
            Place p = placeRepo.save(place);
            user.setPlace(place);
            userRepo.save(user);
            String gallery = p.createGallery();
            if(!imageFile.isEmpty()) {
                try {
                    System.out.println("Storing main image at: " + gallery + imageName);
                    //main images are not store in the folder
                    Utils.storeImageInGallery(gallery + imageName, imageFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return p;
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + uid + " not found")) ;
    }

    @PutMapping("/users/{uid}/places/{pid}")
    public Place updatePlace(
            @PathVariable("uid") int uid,
            @PathVariable("pid") int pid,
            @Valid Place p,
            @RequestParam("image") MultipartFile imageFile
    ){
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
                    String gallery = place.createGallery();
                    //remove old one
                    Utils.deleteImage(gallery + place.getMainImage());

                    String imageExt = FilenameUtils.getExtension(imageFile.getOriginalFilename());
                    System.out.println("Main image extension is: " + imageExt);
                    String imageName = "main." + imageExt;
                    place.setMainImage(imageName);
                    System.out.println("Storing main image at: " + gallery + imageName);
                    //main images are not store in the folder
                    Utils.storeImageInGallery(gallery + imageName, imageFile.getBytes());
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

    @GetMapping("/places/{pid}/mainimage")
    public ResponseEntity<byte[]> getMainImage(@PathVariable("pid") int pid){
        return placeRepo.findById(pid).map(place -> {
            try {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(Utils.getImageBytes(place, place.getMainImage()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).orElseThrow(() -> new ResourceNotFoundException("Place not found with id " + pid));
    }

    //checks if the given user has the given place
    public void validateUserNplace(int uid, int pid){
        if(!userRepo.existsById(uid))
            throw new ResourceNotFoundException("UserID " + uid + " not found");
        Optional<User> owner = userRepo.findById(uid);
        if(owner.isPresent())
            if(owner.get().getPlace().getId() != pid)
                throw new ActionNotAllowedException("Place with PlaceID " + pid + " is not of users with UserID " + uid);
    }

    @GetMapping("/places/{pid}/users")
    public User getPlaceOwner(@PathVariable("pid") int pid){
        return placeRepo.findById(pid).map(place ->{
            return place.getUser();
        }).orElseThrow(() -> new ResourceNotFoundException("Place with Id: " + pid + "not found"));
    }

    @GetMapping("/search")
    public List<Place> searchPlaces(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam("uid") int uid,
            @RequestParam("type")String type,
            @RequestParam("from")Date from,
            @RequestParam("to")Date to,
            @RequestParam("lat")double lat,
            @RequestParam("lon")double lon,
            @RequestParam("num")int num){
        System.out.println("SEARCHING");
        System.out.println("type: " + type);
        System.out.println("from: " + from);
        System.out.println("to: " + to);
        System.out.println("lat: " + lat);
        System.out.println("lon: " + lon);
        System.out.println("num: " + num);

        System.out.println("pageNo: " + pageNo);
        System.out.println("pageSize: " + pageSize);
        double minLat = lat - 0.1f;
        double maxLat = lat + 0.1f;
        double minLong = lon - 0.1f;
        double maxLong = lon + 0.1f;
        List<Place> closePlaces = placeRepo.getClosePlaces(minLat, maxLat, minLong, maxLong);
        System.out.println("Found: " + closePlaces.size() + " close places");
        List<Place> acceptedPlaces = new ArrayList<>();
        if(closePlaces.size()>0){
            User u = userRepo.findById(uid).get();
            Search newSearch = new Search(lat, lon, u);
            searchRepo.save(newSearch);
            System.out.println("new search stored!");
        }
        for(Place p: closePlaces){
            System.out.println("looking the: " + p.getAddress());
            //check the cost!
            System.out.println(p.getMinCost() +" > " +  num + " * " + p.getCostPerPerson());
            if(p.getMinCost() > num * p.getCostPerPerson())
                continue;//ignore it
            System.out.println("Cost is ok");
            //check the type
            if(!type.equals(p.getType()))
                continue;
            System.out.println("Type is ok");
            //check availabilities
            for (Availability av: p.getAvailabilities()){
                System.out.println("Availability: " + av.toString());
                if((av.getFrom().before(from) || av.getFrom().equals(from))
                && (av.getTo().after(to) || av.getTo().equals(to))){
                    System.out.println("accepted place: " + p.getAddress());
                    acceptedPlaces.add(p);
                    break;
                }
            }
        }
        int startIndex = pageNo * pageSize;
        int endIndex = (pageNo * pageSize) + pageSize -1;
        System.out.println("Start: " + startIndex + " End: " + endIndex);
        System.out.println("accepted results ::" + acceptedPlaces.size());
        if(acceptedPlaces.size() == 0)
            return acceptedPlaces;
        if(acceptedPlaces.size() >= startIndex && acceptedPlaces.size() > endIndex) {
            System.out.println("Case 1");
            return acceptedPlaces.subList(startIndex, endIndex);
        }
        else if(acceptedPlaces.size() >= startIndex && acceptedPlaces.size() < endIndex) {
            System.out.println("Case 2");
            return acceptedPlaces.subList(startIndex, acceptedPlaces.size());
        }
        else {
            System.out.println("Case 3");
            return null;
        }

    }

}
