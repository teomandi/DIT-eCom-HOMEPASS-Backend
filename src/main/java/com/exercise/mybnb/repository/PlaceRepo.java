package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepo extends JpaRepository<Place, Integer> {

    Place findByUserId(int uid);
    Optional<Place> findByIdAndUserId(int pid, int uid);

    @Query("SELECT p FROM Place p where p.latitude >= ?1 and p.latitude <= ?1 and p.longitude >= ?3 and p.longitude <= ?5")
    List<Place> getClosePlaces(double minLat,
                             double maxLat,
                             double minLong,
                             double maxLong);
}
