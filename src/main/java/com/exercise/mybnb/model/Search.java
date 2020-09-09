package com.exercise.mybnb.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="searches")
public class Search extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="latitude")
    private double latitude;
    @Column(name="longitude")
    private double longitude;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Search(double latitude, double longitude, User user) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
