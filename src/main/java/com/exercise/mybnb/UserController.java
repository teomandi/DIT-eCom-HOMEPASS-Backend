package com.exercise.mybnb;

import com.exercise.mybnb.model.User;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/user")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUser(@PathVariable("id") int uid){
        return userRepo.findById(uid);
    }

    @PostMapping("/user")
    public User addUser(User u){
        userRepo.save(u);
        return u;
    }

    @PutMapping("/user")
    public User updateUser(User u){
        userRepo.save(u);
        return u;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") int uid){
        User u = userRepo.getOne(uid);
        userRepo.delete(u);;
        return "{ 'message' : 'user deleted with success'}";
    }
}
