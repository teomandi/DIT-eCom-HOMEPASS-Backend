package com.exercise.mybnb.model;

import com.exercise.mybnb.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;


@Entity
@Table(name="users")
public class User extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="username", unique=true, nullable=false)
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="email")
    private String email;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="phone")
    private String phone;
    @Column(name="is_host")
    private boolean isHost = false;
    @Column(name="image_name")
    private String imageName = "default_user.png";
    @Column(name="address")
    private String address;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    @JsonIgnore
    private Place place;
    @OneToMany(mappedBy = "user")
    private Set<Rating> ratings;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private Set<Message> sendedMessages;
    @OneToMany(mappedBy = "reciever")
    @JsonIgnore
    private Set<Message> recievedMessages;
    @OneToMany(mappedBy = "hoster")
    @JsonIgnore
    private Set<Message> messagesHoster;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Search> searches;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int uid) {
        this.id = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Set<Message> getSendedMessages() {
        return sendedMessages;
    }

    public void setSendedMessages(Set<Message> sendedMessages) {
        this.sendedMessages = sendedMessages;
    }

    public Set<Message> getRecievedMessages() {
        return recievedMessages;
    }

    public void setRecievedMessages(Set<Message> recievedMessages) {
        this.recievedMessages = recievedMessages;
    }

    public Set<Message> getMessagesHoster() {
        return messagesHoster;
    }

    public void setMessagesHoster(Set<Message> messagesHoster) {
        this.messagesHoster = messagesHoster;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
