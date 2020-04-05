package com.exercise.mybnb;

import com.exercise.mybnb.model.User;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MultiUser {
    private User user;
    private MultipartFile imageFile;

    public MultiUser(String username,
                     String password,
                     String email,
                     String firstName,
                     String lastName,
                     String phone,
                     String imagePath,
                     MultipartFile imageFile) {
        this.user = new User(username, password, email, firstName, lastName, phone, imagePath) ;
        this.imageFile = imageFile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imgFile) {
        this.imageFile = imgFile;
    }


    public boolean storeImage(){
        String imgFolder = "/home/teomandi/bnb_app_photos/users/";
        String imagName = user.getUsername() + "." + FilenameUtils.getExtension(this.imageFile.getOriginalFilename());
        byte[] imgContent;
        try {
            imgContent = this.imageFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no image file found");
            return false;
        }
        System.out.println("Storing: " + imagName);
        Path path = Paths.get(imgFolder + imagName);
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
