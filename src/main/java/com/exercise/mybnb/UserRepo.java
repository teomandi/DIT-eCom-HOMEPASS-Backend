package com.exercise.mybnb;

import com.exercise.mybnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
