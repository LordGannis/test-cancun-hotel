package com.alten.cancun.hotel.modifybook.controller;

import com.alten.cancun.hotel.exception.BookNotFoundException;
import com.alten.cancun.hotel.exception.InvalidRangeDatesException;
import com.alten.cancun.hotel.exception.NoAvailableDatesException;
import com.alten.cancun.hotel.exception.RoomNotFoundException;
import com.alten.cancun.hotel.exception.UserNotFoundException;
import com.alten.cancun.hotel.mail.MailService;
import com.alten.cancun.hotel.modifybook.ModifyBookServiceApplication;
import com.alten.cancun.hotel.modifybook.configuration.H2JPAConfig;
import com.alten.cancun.hotel.modifybook.controller.vo.BookVO;
import com.alten.cancun.hotel.modifybook.repository.BookRepository;
import com.alten.cancun.hotel.modifybook.repository.RoomRepository;
import com.alten.cancun.hotel.modifybook.repository.UserRepository;
import com.alten.cancun.hotel.modifybook.service.ModifyBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        ModifyBookServiceApplication.class,
        H2JPAConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = "classpath:db/create_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/dump_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ModifyBookControllerTest {

    private RestTemplate restTemplate;

    @Mock
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookRepository bookRepository;

    private ModifyBookController modifyBookController;

    @BeforeEach
    void beforeRunTests() {
        restTemplate = Mockito.mock(RestTemplate.class);
        ModifyBookService modifyBookService = new ModifyBookService(restTemplate, userRepository, roomRepository, bookRepository);
        modifyBookController = Mockito.spy(new ModifyBookController(modifyBookService, mailService));
    }

    @Test
    void whenBookServiceHasWrongParameters_thenIllegalArgumentExceptionIsThrown() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BookVO bookVO = createBookVO("2021-07-01", "2021-07-03");
            bookVO.setRoomId(null);
            modifyBookController.modifyBookRoom(bookVO);
        });
    }

    @Test
    void whenBookServiceHasNoValidBook_thenBookNotFoundExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(BookNotFoundException.class, () -> {
            BookVO bookVO = createBookVO("2021-07-05", "2021-07-06");
            bookVO.setBookId(10L);
            mockAvailableDates();
            modifyBookController.modifyBookRoom(bookVO);
        });

        String expectedMessage = "Book not found for ID=10";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    private void mockAvailableDates() {
        List<String> availableDates = new ArrayList<>();
        availableDates.add("2021-07-05");
        availableDates.add("2021-07-06");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(availableDates);
    }

    @Test
    void whenBookServiceHasMoreThenThreeDays_thenInvalidRangeDatesExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(InvalidRangeDatesException.class, () -> {
            modifyBookController.modifyBookRoom(createBookVO("2021-07-01", "2021-07-04"));
        });

        String expectedMessage = "It is not possible to book more than 3 days.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBookServiceHasDaysNotAvailable_thenNoAvailableDatesExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(NoAvailableDatesException.class, () -> {
            Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(new ArrayList());
            modifyBookController.modifyBookRoom(createBookVO("2021-06-29", "2021-06-30"));
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
            mockAvailableDates();
            modifyBookController.modifyBookRoom(bookVO);
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
            mockAvailableDates();
            modifyBookController.modifyBookRoom(bookVO);
        });

        String expectedMessage = "Room not found for ID=10";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBookServiceSuccessfullyBookAndNoEmailRequired_thenMailServiceNotCalled() {
        mockAvailableDates();
        modifyBookController.modifyBookRoom(createBookVO("2021-07-05", "2021-07-06"));

        Mockito.verify(mailService, Mockito.times(0)).sendEmail(Mockito.any());
    }

    @Test
    void whenBookServiceSuccessfullyBookAndEmailRequired_thenMailServiceCalled() {
        BookVO bookVO = createBookVO("2021-07-05", "2021-07-06");
        bookVO.setEmail("teste@teste.com");
        mockAvailableDates();
        modifyBookController.modifyBookRoom(bookVO);

        Mockito.verify(modifyBookController, Mockito.times(1)).sendEmailToUser(Mockito.any(), Mockito.any());
    }

    private BookVO createBookVO(String startDate, String endDate) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        BookVO bookVO = new BookVO();
        bookVO.setBookId(100L);
        bookVO.setRoomId(1L);
        bookVO.setUserId(2L);
        bookVO.setStartDate(LocalDate.parse(startDate, dtf));
        bookVO.setEndDate(LocalDate.parse(endDate, dtf));

        return bookVO;
    }

}
