package com.exercise.mybnb.controller;

import com.exercise.mybnb.exception.ActionNotAllowedException;
import com.exercise.mybnb.exception.ResourceNotFoundException;
import com.exercise.mybnb.model.Message;
import com.exercise.mybnb.repository.MessageRepo;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessageRepo messageRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/messages")
    public List<Message> getAllMessage(){
        return messageRepo.findAll();
    }

    @PostMapping("/messages/{sid}/{rid}/{hid}")
    public Message createMessage(
            @PathVariable("sid") int sender_id,
            @PathVariable("rid") int reciever_id,
            @PathVariable("hid") int hoster_id,
            @RequestParam("text") String text)
    {
        if( hoster_id != reciever_id && hoster_id != sender_id ){
            throw new ActionNotAllowedException("Host in none of the taking part users");
        }
        return userRepo.findById(sender_id).map(sender -> {
            return userRepo.findById(reciever_id).map(reciever -> {
                return userRepo.findById(hoster_id).map(hoster -> {
                    Message msg = new Message();
                    msg.setSender(sender);
                    msg.setReciever(reciever);
                    msg.setHoster(hoster);
                    msg.setText(text);
                    return messageRepo.save(msg);
                }).orElseThrow(() -> new ResourceNotFoundException("Hoster with id " + hoster_id + " not found"));
            }).orElseThrow(() -> new ResourceNotFoundException("Reciever with id " + reciever_id + " not found"));
        }).orElseThrow(() -> new ResourceNotFoundException("Sender with id " + sender_id + " not found"));
    }

    @GetMapping("messages/host/{u1id}/{u2id}/chat")
    public List<Message> getUsersChatWithHost(
            @PathVariable("u1id") int user_1, //<-host
            @PathVariable("u2id") int user_2

    ){
        return userRepo.findById(user_1).map(user1 -> {
            return userRepo.findById(user_2).map(user2 -> {
                return messageRepo.findMessagesBetweenUsersAsHost(user1, user2);
            }).orElseThrow(() -> new ResourceNotFoundException("User2 with id " + user_2 + " not found"));
        }).orElseThrow(() -> new ResourceNotFoundException("User1 with id " + user_1 + " not found"));
    }

    @GetMapping("messages/{u1id}/{u2id}/chat")
    public List<Message> getUsersChatWithoutHost(
            @PathVariable("u1id") int user_1, //this is the user that makes the requests
            @PathVariable("u2id") int user_2
    ){
        return userRepo.findById(user_1).map(user1 -> {
            return userRepo.findById(user_2).map(user2 -> {
                return messageRepo.findMessagesBetweenUsersAsNotHost(user1, user2);
            }).orElseThrow(() -> new ResourceNotFoundException("User2 with id " + user_2 + " not found"));
        }).orElseThrow(() -> new ResourceNotFoundException("User1 with id " + user_1 + " not found"));
    }

    //returns the latest message from each user
    @GetMapping("messages/host/{uid}")
    public List<Message> getUsersChatWithoutHost(@PathVariable("uid") int uid){
        return userRepo.findById(uid).map(user -> {
            List<Message> latestMessagePerChat = new ArrayList<>();
            List<Message> userMessages = messageRepo.findMessageByHosterOrderByCreatedAtDesc(user);
            List<Integer> includedUsers = new ArrayList<>();
            for(Message msg: userMessages){
                if(msg.getSender().getId() == user.getId()){
                    //our user send the message so check the reciever
                    if(!includedUsers.contains(msg.getReciever().getId())){
                        latestMessagePerChat.add(msg);
                        includedUsers.add(msg.getReciever().getId());
                    }
                }else {
                    //our user got the message so check the sender
                    if(!includedUsers.contains(msg.getSender().getId())){
                        latestMessagePerChat.add(msg);
                        includedUsers.add(msg.getSender().getId());
                    }
                }
            }
            return latestMessagePerChat;
        }).orElseThrow(() -> new ResourceNotFoundException("User with id " + uid + " not found"));
    }


}
