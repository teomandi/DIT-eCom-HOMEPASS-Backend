package com.exercise.mybnb.controller;

import com.exercise.mybnb.exception.ResourceAlreadyExistException;
import com.exercise.mybnb.model.User;
import com.exercise.mybnb.repository.UserRepo;
import com.exercise.mybnb.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCrypt; // <-----
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable){
        return userRepo.findAll(pageable);
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable("id") int uid){
        return userRepo.findById(uid);
    }

    @GetMapping("/users/{username}/username")
    public User getUserByUsername(@PathVariable("username") String username){
        return  userRepo.findByUsername(username);
    }

    @GetMapping("/users/{id}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable("id") int uid) throws IOException {
        Optional<User> u = userRepo.findById(uid);
        System.out.println("image for User: " + u.get().getUsername());
        byte[] image = u.get().getUserImageBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @PostMapping("/users")
    public User createUser(User u, @RequestParam("picture") MultipartFile imageFile) throws IOException {
        boolean exist = userRepo.existsUserByUsername(u.getUsername());
        if(exist)
            throw new ResourceAlreadyExistException("User already exists");
        if(imageFile != null) {
            String imageName = u.getUsername() + "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
            u.setImageName(imageName);
            Utils.storeImage("users/"+imageName, imageFile.getBytes());
        }
        else
            System.out.println("ImageFile is null");
        System.out.println("Before " + u.getPassword());
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        System.out.println("After " + u.getPassword());
        return userRepo.save(u);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @Valid User u, MultipartFile imageFile){
        return userRepo.findById(id).map(user -> {
            user.setUsername(u.getUsername());
            user.setPassword(u.getPassword()); //no
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
