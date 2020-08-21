package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RuleRepo extends JpaRepository<Rule, Integer> {
    Optional<Set<Rule>> findByPlaceId(int pid);
}
