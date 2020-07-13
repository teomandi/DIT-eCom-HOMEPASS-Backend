package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.PlaceImage;
import com.exercise.mybnb.repository.PlaceImageRepo;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PlaceImageController {
    @Autowired
    PlaceImageRepo imageRepo;
    @Autowired
    PlaceRepo placeRepo;

    @GetMapping("/images")
    public List<PlaceImage> getAllImages() {
        return imageRepo.findAll();
    }

    @GetMapping("/images/{id}")
    public Optional<PlaceImage> getImage(@PathVariable("id") int id) {
        return imageRepo.findById(id);
    }

    @GetMapping("/places/{pid}/images")
    public Page<PlaceImage> getAllImagesByPlaceId(@PathVariable("pid") int pid, Pageable pageable) {
        return imageRepo.findByPlaceId(pid, pageable);
    }

    @PostMapping("/places/{pid}/images")
    public ResponseEntity<?> createImage(@PathVariable("pid") int pid,
                                         MultipartFile[] files) {
        if (files == null)
            throw new ResourceNotFoundException("Image file not found");
        return placeRepo.findById(pid).map(place -> {
            Utils.makeDir(pid);
            for (MultipartFile file : files) {
                PlaceImage image = new PlaceImage();
                image.setFilename(file.getOriginalFilename());
                image.setPlace(place);
                imageRepo.save(image);
                //store the image
                try {
                    Utils.storeImage("places/" + pid + "/" + file.getOriginalFilename(), file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

    //PUT????

    @DeleteMapping("/places/{pid}/images/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("pid") int pid,
                                         @PathVariable("id") int id) {
        return imageRepo.findByIdAndPlaceId(id, pid).map(image -> {
            //ALSO DELETE THE IMAGE
            Utils.deletePlaceImage(pid+"/" + image.getFilename());
            imageRepo.delete(image);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Image not found with id "
                + id + " and PlaceID " + pid));
    }

    @PostMapping("/random")
    public Map<String, String> randomRequest(@RequestParam MultiValueMap<String, String> data){
        System.out.println(data.get("key1").toString());
        System.out.println(data.get("key2"));
        System.out.println(data.get("key3"));

        Map<String, String> resp = new HashMap<String, String>();
        resp.put("KEY1", String.valueOf(data.get("key1")).toUpperCase());
        resp.put("KEY2", String.valueOf(data.get("key2")).toUpperCase());
        resp.put("KEY3", String.valueOf(data.get("key3")).toUpperCase());

        return resp;
    }

}
