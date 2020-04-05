package com.exercise.mybnb;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MultiPlace {
    private int placeId;
    private MultipartFile imageFile;

    public MultiPlace(int placeId, MultipartFile imageFile) {
        this.placeId = placeId;
        this.imageFile = imageFile;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public boolean storeImage(){
        String placeDir = "/home/teomandi/bnb_app_photos/places/" + this.placeId + "/";
        File directory = new File(placeDir);
        if (! directory.exists()) {
            directory.mkdir();
        }
        String namePath =  this.imageFile.getOriginalFilename();
        byte[] imgContent;
        try {
            imgContent = this.imageFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no image file found");
            return false;
        }
        Path path = Paths.get(placeDir + namePath);
        try {
            Files.write(path, imgContent);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not store image");
            return false;
        }
        System.out.println("Image stored");
        return true;
    }
}
