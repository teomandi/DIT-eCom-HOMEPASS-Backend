package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Message;
import com.exercise.mybnb.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessageRepo messageRepo;

    @GetMapping("/messages")
    public List<Message> getAllMessage(){
        return messageRepo.findAll();
    }

    @PostMapping("/messages")
    public Message createMessage(Message msg)
    {
        return messageRepo.save(msg);
    }
}
