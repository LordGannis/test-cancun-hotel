package com.alten.cancun.hotel.user.repository;

import com.alten.cancun.hotel.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

}
