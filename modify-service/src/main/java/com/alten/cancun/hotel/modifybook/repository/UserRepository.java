package com.alten.cancun.hotel.modifybook.repository;

import com.alten.cancun.hotel.modifybook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
