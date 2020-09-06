package com.exercise.mybnb.controller;

import com.exercise.mybnb.exception.ResourceNotFoundException;
import com.exercise.mybnb.model.Availability;
import com.exercise.mybnb.model.Reservation;
import com.exercise.mybnb.repository.AvailabilityRepo;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.ReservationRepo;
import com.exercise.mybnb.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
public class ReservationController {
    @Autowired
    PlaceRepo placeRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    AvailabilityRepo availabilityRepo;
    @Autowired
    ReservationRepo reservationRepo;

    @PostMapping("/reservations/places/{pid}/users/{uid}")
    public boolean createReservation(
            @PathVariable("pid") int pid,
            @PathVariable("uid") int uid,
            @RequestParam("start") Date start,
            @RequestParam("end") Date end
    ){
        System.out.println("Creating reservations!");
        return userRepo.findById(uid).map(user -> {
            System.out.println("User found: " + user.getUsername());
            return placeRepo.findById(pid).map(place -> {
                System.out.println("Place found: " + place.getAddress());
                Set<Availability> availabilities = place.getAvailabilities();
                if(availabilities.isEmpty()){
                    System.out.println("no availabilities found!!!");
                    return false;
                }
                System.out.println("checking avs for: " + start + "---" + end);
                for(Availability av: availabilities){
                    System.out.println("checking av: " + av.toString());
                    if(av.getFrom().before(start) ||av.getFrom().equals(start)){
                        System.out.println("start is ok");
                        if(av.getTo().after(end) || av.getTo().equals(end)){
                            System.out.println("end is ok: Reservation accepted!");
                            //split the availability and remove it
                            if(!av.getFrom().equals(start)){
                                Availability splitA = new Availability();
                                splitA.setFrom(av.getFrom());
                                splitA.setTo(start);
                                splitA.setPlace(place);
                                availabilityRepo.save(splitA);
                            }
                            if(!av.getTo().equals(end)){
                                Availability splitB = new Availability();
                                splitB.setFrom(end);
                                splitB.setTo(av.getTo());
                                splitB.setPlace(place);
                                availabilityRepo.save(splitB);
                            }
                            availabilityRepo.delete(av);
                            System.out.println("new availabilities handled");
                            if(reservationRepo.existsByUserAndPlace(user, place)){
                                System.out.println("Older reservation from the same user in the same place found and it will be DELETED!");
                                Reservation r = reservationRepo.findByUserAndPlace(user, place);
                                reservationRepo.delete(r);
                            }
                            //create the reservation
                            Reservation reservation = new Reservation();
                            reservation.setStart(start);
                            reservation.setEnd(end);
                            reservation.setPlace(place);
                            reservation.setUser(user);
                            reservationRepo.save(reservation);
                            return true;
                        }
                    }
                }
                System.out.println("No available dates found!!!");
                return false;
            }).orElseThrow(() -> new ResourceNotFoundException("Place with id " + pid + " not found!"));
        }).orElseThrow(() -> new ResourceNotFoundException("User with id " + uid + " not found!"));
    }

    @GetMapping("/reservations/places/{pid}/users/{uid}")
    public Reservation getReservation(
            @PathVariable("pid") int pid,
            @PathVariable("uid") int uid
    ){
        System.out.println("Getting reservations!");
        return userRepo.findById(uid).map(user -> {
            System.out.println("User found: " + user.getUsername());
            return placeRepo.findById(pid).map(place -> {
                System.out.println("Place found: " + place.getAddress());
                if(reservationRepo.existsByUserAndPlace(user, place))
                    return reservationRepo.findByUserAndPlace(user, place);
                else
                    throw new ResourceNotFoundException("Reservation not found!");
            }).orElseThrow(() -> new ResourceNotFoundException("Place with id " + pid + " not found!"));
        }).orElseThrow(() -> new ResourceNotFoundException("User with id " + uid + " not found!"));
    }

    @GetMapping("/reservations/places/{pid}/users/{uid}/exist")
    public boolean checkExistence(
            @PathVariable("pid") int pid,
            @PathVariable("uid") int uid
    ){
        System.out.println("checking reservations!");
        return userRepo.findById(uid).map(user -> {
            System.out.println("User found: " + user.getUsername());
            return placeRepo.findById(pid).map(place -> {
                System.out.println("Place found: " + place.getAddress());
                return reservationRepo.existsByUserAndPlace(user, place);
            }).orElseThrow(() -> new ResourceNotFoundException("Place with id " + pid + " not found!"));
        }).orElseThrow(() -> new ResourceNotFoundException("User with id " + uid + " not found!"));
    }

    @GetMapping("/reservations/places/{pid}/users/{uid}/canrate")
    public boolean checkIfUserCanRate(
            @PathVariable("pid") int pid,
            @PathVariable("uid") int uid
    ){
        System.out.println("checking if user can rate!");
        return userRepo.findById(uid).map(user -> {
            System.out.println("User found: " + user.getUsername());
            return placeRepo.findById(pid).map(place -> {
                System.out.println("Place found: " + place.getAddress());
                if(reservationRepo.existsByUserAndPlace(user, place)){
                    Reservation reservation = reservationRepo.findByUserAndPlace(user, place);
                    return new Date().after(reservation.getEnd());
                }
                else
                    return false;
            }).orElseThrow(() -> new ResourceNotFoundException("Place with id " + pid + " not found!"));
        }).orElseThrow(() -> new ResourceNotFoundException("User with id " + uid + " not found!"));
    }
}
