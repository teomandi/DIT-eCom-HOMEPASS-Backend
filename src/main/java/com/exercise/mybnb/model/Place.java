package com.exercise.mybnb.model;

import com.exercise.mybnb.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@Entity
@Table(name="places")
public class Place extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "place")
    @JsonIgnore
    private User user;

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
    private String type;
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

    @OneToMany(mappedBy = "place")
    private Set<Benefit> benefits;
    @OneToMany(mappedBy = "place")
    private Set<Rule> rules;
    @OneToMany(mappedBy = "place")
    private Set<Image> images;
    @OneToMany(mappedBy = "place")
    private Set<Availability> availabilities;
    @OneToMany(mappedBy = "place")
    private Set<Rating> ratings;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setMinCost(int minCost) {
        this.minCost = minCost;
    }

    public int getCostPerPerson() {
        return costPerPerson;
    }

    public void setCostPerPerson(int costPerPerson) {
        this.costPerPerson = costPerPerson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBaths() {
        return baths;
    }

    public void setBaths(int baths) {
        this.baths = baths;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public boolean isLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(boolean livingRoom) {
        this.livingRoom = livingRoom;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Set<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Set<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public Set<Benefit> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<Benefit> benefits) {
        this.benefits = benefits;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public String createGallery(){
        String dir = Utils.getMainPath() + "places/" + id + "/";
        if(Files.notExists(Paths.get(dir))){
            new File(dir).mkdir();
            System.out.println("GalleryDir created: " + dir);
        }
        return dir;
    }
}
