package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<Image, Integer> {

    Page<Image> findByPlaceId(int pid, Pageable pageable);
    Optional<Image> findByIdAndPlaceId(int iid, int pid);
}
