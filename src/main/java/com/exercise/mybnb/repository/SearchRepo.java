package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.Search;
import com.exercise.mybnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepo extends JpaRepository<Search, Integer>  {
    boolean existsByUser(User u);
    List<Search> findByUser(User u);
}
