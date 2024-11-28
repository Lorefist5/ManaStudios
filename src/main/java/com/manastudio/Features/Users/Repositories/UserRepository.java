package com.manastudio.Features.Users.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manastudio.Features.Users.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); 
    User findByEmail(String email);
    
}
