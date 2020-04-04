package com.exercise.mybnb;

import com.exercise.mybnb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepo repo;

    @GetMapping("/user")
    public List<User> getUsers(){
        return repo.findAll();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUser(@PathVariable("id") int uid){
        return repo.findById(uid);
    }

    @PostMapping("/user")
    public User addUser(User u){
        System.out.println("Adding user " + u.getUsername());
        repo.save(u);
        return u;
    }

    @PutMapping("/user")
    public User updateUser(User u){
        repo.save(u);
        return u;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") int uid){
        User u = repo.getOne(uid);
        repo.delete(u);;
        return "Deleted";
    }
}
