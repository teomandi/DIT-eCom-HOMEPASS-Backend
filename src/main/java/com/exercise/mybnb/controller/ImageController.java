package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Image;
import com.exercise.mybnb.repository.ImageRepo;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class ImageController {
    @Autowired
    ImageRepo imageRepo;
    @Autowired
    PlaceRepo placeRepo;

    @GetMapping("/images/names")
    public List<Image> getAllImages() {
        return imageRepo.findAll();
    }

    @GetMapping("/places/{pid}/images")
    public Optional<Set<Image>> getAllImages(@PathVariable("pid") int pid) {
        return imageRepo.findByPlaceId(pid);
    }

    @PostMapping("/places/{pid}/images")
    public ResponseEntity<?> createImages(@PathVariable("pid") int pid,
                                         @RequestParam("images") MultipartFile[] files) {
        System.out.println("Posting images" + pid);
        if (files == null)
            throw new ResourceNotFoundException("Image file not found");
        return placeRepo.findById(pid).map(place -> {
            System.out.println("Place? " + place.getAddress());
            System.out.println("Mults? " + files.length);
            Utils.makeDir(pid);
            for (MultipartFile file : files) {
                System.out.println("image got: " + file.getOriginalFilename());
                String imageExt = FilenameUtils.getExtension(file.getOriginalFilename());
                String filename = place.getId() + "_" + imageRepo.count() + "." + imageExt;
                Image image = new Image();
                image.setFilename(filename);
                image.setPlace(place);
                imageRepo.save(image);
                //store the image
                try {
                    Utils.storeImage("places/" + pid + "/" +filename, file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") int id) {
        return imageRepo.findById(id).map(image -> {
            byte[] imBytes = new byte[0];
            try {
                imBytes = Utils.getImageBytes(image.getPlace(), image.getFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imBytes);
        }).orElseThrow(() -> new ResourceNotFoundException("Image " + id + " not found"));
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> removeImage(@PathVariable("id") int id){
        return imageRepo.findById(id).map(image -> {
            String filepath = Utils.getMainPath() + "places/" + image.getPlace().getId() + "/" + image.getFilename();
            if (Utils.deleteImage(filepath)){
                imageRepo.delete(image);
                return ResponseEntity.ok().build();
            }
            else
                return ResponseEntity.notFound().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Image " + id + " not found"));
    }

    @PutMapping("/places/{pid}/images")
    public ResponseEntity<?> putImages(@PathVariable("pid") int pid,
                                         @RequestParam("images") MultipartFile[] files) {
        System.out.println("putting images" + pid);
        if (files == null)
            throw new ResourceNotFoundException("Image file not found");
        return placeRepo.findById(pid).map(place -> {
            //delete all last images first
            for(Image img: place.getImages()){
                String filepath = Utils.getMainPath() + "places/" + place.getId() + "/" + img.getFilename();
                if (Utils.deleteImage(filepath))
                    System.out.println("Image " + img.toString() + " was deleted!");
                    imageRepo.delete(img);
            }
            System.out.println("Mults files: " + files.length);
            Utils.makeDir(pid);
            for (MultipartFile file : files) {
                System.out.println("image got: " + file.getOriginalFilename());
                String imageExt = FilenameUtils.getExtension(file.getOriginalFilename());
                String filename = place.getId() + "_" + imageRepo.count() + "." + imageExt;
                Image image = new Image();
                image.setFilename(filename);
                image.setPlace(place);
                imageRepo.save(image);
                //store the image
                try {
                    Utils.storeImage("places/" + pid + "/" +filename, file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

}
