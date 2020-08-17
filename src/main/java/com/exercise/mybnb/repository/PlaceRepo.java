package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepo extends JpaRepository<Place, Integer> {

    Place findByUserId(int uid);
    Optional<Place> findByIdAndUserId(int pid, int uid);

}
