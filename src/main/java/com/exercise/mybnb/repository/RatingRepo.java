package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Benefit;
import com.exercise.mybnb.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Integer> {
    Optional<Set<Rating>> findByPlaceId(int pid);

}