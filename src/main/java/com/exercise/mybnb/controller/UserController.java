package com.exercise.mybnb.controller;

import com.exercise.mybnb.MultiUser;
import com.exercise.mybnb.model.User;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/user")
    public Page<User> getAllUsers(Pageable pageable){
        return userRepo.findAll(pageable);
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUser(@PathVariable("id") int uid){
        return userRepo.findById(uid);
    }

    @PostMapping("/user")
    public String createUser(@Valid @RequestBody  MultiUser mu){
        User u = mu.getUser();
        boolean exist = userRepo.existsUserByUsername(u.getUsername());

        if(exist)
            return "{'error' : 'username already exists'}";

        boolean res = mu.storeImage();
        userRepo.save(u);
        return "{ 'success' : 'user added', 'image' " + res + " } " ;
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User u){
        return userRepo.findById(id).map(user -> {
            user.setUsername(u.getUsername());
            user.setEmail(u.getEmail());
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());
            user.setPhone(u.getPhone());
            //not implemented for new image
            return userRepo.save(u);
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + id + " not found"));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
        return userRepo.findById(id).map(user -> {
            userRepo.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("UserID " + id + " not found"));
    }
}
