package com.alten.cancun.hotel.cancelbook.controller;

import com.alten.cancun.hotel.cancelbook.CancelBookServiceApplication;
import com.alten.cancun.hotel.cancelbook.configuration.H2JPAConfig;
import com.alten.cancun.hotel.cancelbook.repository.BookRepository;
import com.alten.cancun.hotel.cancelbook.service.CancelBookService;
import com.alten.cancun.hotel.exception.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = {
        CancelBookServiceApplication.class,
        H2JPAConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = "classpath:db/create_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/dump_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CancelBookControllerTest {

    @Autowired
    private CancelBookController cancelBookController;

    @Autowired
    private CancelBookService cancelBookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenCancelRequestForNoExistingBook_thenBookNotFoundExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(BookNotFoundException.class, () -> {
            cancelBookController.cancelBookRoom(10L);
        });

        String expectedMessage = "Book not found for ID=10";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenCancelRequestForExistingBook_thenBookSavedSuccessfully() {
        ResponseEntity responseEntity = cancelBookController.cancelBookRoom(101L);

        Assertions.assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
    }

}
