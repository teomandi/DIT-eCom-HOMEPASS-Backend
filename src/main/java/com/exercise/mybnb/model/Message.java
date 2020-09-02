package com.exercise.mybnb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="messages")
public class Message extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="text")
    private String text;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User sender;

    @ManyToOne
    @JoinColumn(name="reciever_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User reciever;

    @ManyToOne
    @JoinColumn(name="hoster_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User hoster;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public User getHoster() {
        return hoster;
    }

    public void setHoster(User hoster) {
        this.hoster = hoster;
    }
}
