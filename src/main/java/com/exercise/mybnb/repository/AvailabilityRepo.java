package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AvailabilityRepo extends JpaRepository<Availability, Integer> {
    Optional<Set<Availability>> findByPlaceId(int pid);
}
