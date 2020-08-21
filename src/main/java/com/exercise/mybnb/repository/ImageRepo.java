package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ImageRepo extends JpaRepository<Image, Integer> {

    Optional<Set<Image>> findByPlaceId(int pid);
}
