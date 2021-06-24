package com.alten.cancun.hotel.book.repository;

import com.alten.cancun.hotel.book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
