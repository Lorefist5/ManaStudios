package com.manastudio.Features.Users.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Users.Exceptions.UserNotFoundException;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;

@Service
public class UserFetchingService {

    @Autowired
    private UserRepository userRepository;

    public Result<User> fetchUserById(Long id) {
    	
    	Optional<User> userFetchingResults = userRepository.findById(id); 

        if (!userFetchingResults.isPresent()) { 
            return Result.from(new UserNotFoundException("User id not found."));
        }

        // Return the User if found
        return Result.from(userFetchingResults.get());
    }
}
