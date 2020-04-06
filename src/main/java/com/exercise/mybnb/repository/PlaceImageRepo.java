package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.PlaceImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceImageRepo extends JpaRepository<PlaceImage, Integer> {

    Page<PlaceImage> findByPlaceId(int pid, Pageable pageable);
    Optional<PlaceImage> findByIdAndPlaceId(int iid, int pid);
}
