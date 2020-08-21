package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface BenefitRepo extends JpaRepository<Benefit, Integer> {
    Optional<Set<Benefit>> findByPlaceId(int pid);
}
