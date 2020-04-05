package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepo extends JpaRepository<Place, Integer> {

    Page<Place> findByOwnerId(int uid, Pageable pageable);
    Optional<Place> findByIdAndOwnerId(int pid, int uid);

}
