package com.exercise.mybnb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="places")
public class Place extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User owner;
    @Column(name="main_image")
    private String mainImage;
    @Column(name="address")
    private String address;
    @Column(name="latitude")
    private String latitude;
    @Column(name="longitude")
    private String longitude;
    @Column(name="max_guests")
    private int maxGuests;
    @Column(name="min_cost")
    private int minCost;
    @Column(name="cost_per_person")
    private int costPerPerson;
    @Column(name="type")
    private int type;
    @Column(name="description")
    private String description;
    @Column(name="beds")
    private int beds;
    @Column(name="baths")
    private int baths;
    @Column(name="bedrooms")
    private int bedrooms;
    @Column(name="living_room")
    private boolean livingRoom;
    @Column(name="area")
    private int area;

    // availability, images, extras (rules||benefits)



}
