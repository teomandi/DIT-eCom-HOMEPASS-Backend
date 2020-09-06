package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.model.Reservation;
import com.exercise.mybnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    boolean existsByUserAndPlace(User u, Place p);
    Reservation findByUserAndPlace(User u, Place p);
}
