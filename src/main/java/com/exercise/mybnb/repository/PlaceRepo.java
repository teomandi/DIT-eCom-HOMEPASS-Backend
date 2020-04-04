package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository<Place, Integer> {


}
