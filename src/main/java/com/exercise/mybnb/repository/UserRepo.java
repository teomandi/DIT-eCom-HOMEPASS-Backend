package com.exercise.mybnb.repository;

import com.exercise.mybnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    public boolean existsUserByUsername(String username);
}
