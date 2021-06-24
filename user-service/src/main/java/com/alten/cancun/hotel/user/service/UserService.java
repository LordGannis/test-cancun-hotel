package com.alten.cancun.hotel.user.service;

import com.alten.cancun.hotel.exception.UserNotFoundException;
import com.alten.cancun.hotel.user.entity.User;
import com.alten.cancun.hotel.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User Not Found for login = " + login);
        }

        return user;
    }

}
