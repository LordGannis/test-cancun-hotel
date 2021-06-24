package com.alten.cancun.hotel.user.controller;

import com.alten.cancun.hotel.user.service.UserService;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{login}")
    public ResponseEntity findByLogin(
            @ApiParam(value = "User login", required = true) @PathVariable("login") String login) {
        LOGGER.info("User findByLogin - login={}", login);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByLogin(login));
    }

}
