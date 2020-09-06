package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Message;
import com.exercise.mybnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer> {


    @Query("SELECT m FROM Message m WHERE m.hoster=?1 and ((m.reciever=?1 and m.sender=?2) or (m.reciever=?2 and m.sender=?1)) order by  m.createdAt ")
    List<Message> findMessagesBetweenUsersAsHost(User user1, User user2);

    @Query("SELECT m FROM Message m WHERE m.hoster<>?1 and ((m.reciever=?1 and m.sender=?2) or (m.reciever=?2 and m.sender=?1)) order by  m.createdAt ")
    List<Message> findMessagesBetweenUsersAsNotHost(User user1, User user2);

    List<Message> findMessageByHosterOrderByCreatedAtDesc(User hoster);

    @Query("SELECT m FROM Message m WHERE m.hoster<>?1 and (m.reciever=?1 or m.sender=?1) order by  m.createdAt desc ")
    List<Message> findUserMessages(User user);



}
