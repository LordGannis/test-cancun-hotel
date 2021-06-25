package com.alten.cancun.hotel.book.controller;

import com.alten.cancun.hotel.book.BookServiceApplication;
import com.alten.cancun.hotel.book.configuration.H2JPAConfig;
import com.alten.cancun.hotel.book.controller.vo.BookVO;
import com.alten.cancun.hotel.book.entity.Book;
import com.alten.cancun.hotel.book.repository.BookRepository;
import com.alten.cancun.hotel.book.repository.RoomRepository;
import com.alten.cancun.hotel.book.repository.UserRepository;
import com.alten.cancun.hotel.book.service.BookService;
import com.alten.cancun.hotel.exception.InvalidRangeDatesException;
import com.alten.cancun.hotel.exception.NoAvailableDatesException;
import com.alten.cancun.hotel.exception.RoomNotFoundException;
import com.alten.cancun.hotel.exception.UserNotFoundException;
import com.alten.cancun.hotel.mail.MailService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {
        BookServiceApplication.class,
        H2JPAConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = "classpath:db/create_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/dump_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookRepository bookRepository;

    private BookController bookController;

    @BeforeAll
    void beforeRunTests() {
        BookService bookService = new BookService(restTemplate, userRepository, roomRepository, bookRepository);
        bookController = Mockito.spy(new BookController(bookService, mailService));
    }

    @Test
    void whenListAllBookingsForNoExistingUser_thenUserNotFoundExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            bookController.listAll(100L);
        });

        String expectedMessage = "User not found for ID=100";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenListAllBookingsForUserWithNoBookings_thenMethodReturnEmptyList() {
        ResponseEntity<List<Book>> responseEntity = bookController.listAll(3L);

        Assertions.assertTrue(responseEntity.getBody().isEmpty());
    }

    @Test
    void whenListAllBookingsForUserWithBookings_thenMethodReturnNonEmptyList() {
        ResponseEntity<List<Book>> responseEntity = bookController.listAll(1L);

        Assertions.assertTrue(!responseEntity.getBody().isEmpty());
    }

    @Test
    void whenBookServiceHasWrongParameters_thenIllegalArgumentExceptionIsThrown() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BookVO bookVO = createBookVO("2021-07-01", "2021-07-03");
            bookVO.setRoomId(null);
            bookController.bookRoom(bookVO);
        });
    }

    @Test
    void whenBookServiceHasMoreThenThreeDays_thenInvalidRangeDatesExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(InvalidRangeDatesException.class, () -> {
            bookController.bookRoom(createBookVO("2021-07-01", "2021-07-04"));
        });

        String expectedMessage = "It is not possible to book more than 3 days.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBookServiceHasDaysNotAvailable_thenNoAvailableDatesExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(NoAvailableDatesException.class, () -> {
            Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(new ArrayList());
            bookController.bookRoom(createBookVO("2021-07-01", "2021-07-03"));
        });

        String expectedMessage = "One or more dates informed is not available.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBookServiceHasNoValidUser_thenUserNotFoundExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            BookVO bookVO = createBookVO("2021-07-05", "2021-07-06");
            bookVO.setUserId(10L);
            Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(Arrays.asList("2021-07-05", "2021-07-06"));
            bookController.bookRoom(bookVO);
        });

        String expectedMessage = "User not found for ID=10";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBookServiceHasNoValidRoom_thenRoomNotFoundExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(RoomNotFoundException.class, () -> {
            BookVO bookVO = createBookVO("2021-07-05", "2021-07-06");
            bookVO.setRoomId(10L);
            Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(Arrays.asList("2021-07-05", "2021-07-06"));
            bookController.bookRoom(bookVO);
        });

        String expectedMessage = "Room not found for ID=10";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBookServiceSuccessfullyBookAndNoEmailRequired_thenMailServiceNotCalled() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(Arrays.asList("2021-07-05", "2021-07-06"));
        bookController.bookRoom(createBookVO("2021-07-05", "2021-07-06"));

        Mockito.verify(mailService, Mockito.times(0)).sendEmail(Mockito.any());
    }

    @Test
    void whenBookServiceSuccessfullyBookAndEmailRequired_thenMailServiceCalled() {
        BookVO bookVO = createBookVO("2021-07-05", "2021-07-06");
        bookVO.setEmail("teste@teste.com");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(Arrays.asList("2021-07-05", "2021-07-06"));
        bookController.bookRoom(bookVO);

        Mockito.verify(bookController, Mockito.times(1)).sendEmailToUser(Mockito.any(), Mockito.any());
    }

    private BookVO createBookVO(String startDate, String endDate) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        BookVO bookVO = new BookVO();
        bookVO.setRoomId(1L);
        bookVO.setUserId(2L);
        bookVO.setStartDate(LocalDate.parse(startDate, dtf));
        bookVO.setEndDate(LocalDate.parse(endDate, dtf));

        return bookVO;
    }

}
