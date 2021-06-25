package com.alten.cancun.hotel.user.controller;

import com.alten.cancun.hotel.exception.UserNotFoundException;
import com.alten.cancun.hotel.user.UserServiceApplication;
import com.alten.cancun.hotel.user.configuration.H2JPAConfig;
import com.alten.cancun.hotel.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = {
        UserServiceApplication.class,
        H2JPAConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = "classpath:db/create_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/dump_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    public void whenUserIsNotFound_thenUserNotFoundExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userController.findByLogin("test_fail");
        });

        String expectedMessage = "User Not Found for login=test_fail";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenUserIsFound_thenReturnTheUser() {
        ResponseEntity<User> responseEntity = userController.findByLogin("sophia_jackson");

        Assertions.assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
        Assertions.assertTrue(responseEntity.getBody().getId().equals(1L));
    }

}
