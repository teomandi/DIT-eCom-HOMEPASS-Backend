package com.exercise.mybnb.controller;

import com.exercise.mybnb.exception.ResourceAlreadyExistException;
import com.exercise.mybnb.model.User;
import com.exercise.mybnb.repository.UserRepo;
import com.exercise.mybnb.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCrypt; // <-----
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable){
        return userRepo.findAll(pageable);
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable("id") int uid){
        return userRepo.findById(uid);
    }

    @PostMapping("/users")
    public User createUser(User u, MultipartFile imageFile) throws IOException {
        boolean exist = userRepo.existsUserByUsername(u.getUsername());
        if(exist)
            throw new ResourceAlreadyExistException("User already exists");
        if(imageFile != null) {
            String imageName = u.getUsername() + "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
            u.setImageName(imageName);
            Utils.storeImage("users/"+imageName, imageFile.getBytes());
        }
        return userRepo.save(u);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @Valid User u, MultipartFile imageFile){
        return userRepo.findById(id).map(user -> {
            user.setUsername(u.getUsername());
            user.setPassword(u.getPassword());
            user.setEmail(u.getEmail());
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());
            user.setPhone(u.getPhone());
            if(imageFile != null) {
                String imageName = u.getUsername() + "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
                u.setImageName(imageName);
                try {
                    Utils.storeImage("users/" + imageName, imageFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return userRepo.save(u);
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + id + " not found"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
        return userRepo.findById(id).map(user -> {
            userRepo.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + id + " not found"));
    }
}
