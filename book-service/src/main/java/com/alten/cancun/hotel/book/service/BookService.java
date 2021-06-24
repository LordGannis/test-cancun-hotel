package com.alten.cancun.hotel.book.service;

import com.alten.cancun.hotel.book.controller.vo.BookVO;
import com.alten.cancun.hotel.book.entity.Book;
import com.alten.cancun.hotel.book.entity.Room;
import com.alten.cancun.hotel.book.entity.User;
import com.alten.cancun.hotel.book.repository.BookRepository;
import com.alten.cancun.hotel.book.repository.RoomRepository;
import com.alten.cancun.hotel.book.repository.UserRepository;
import com.alten.cancun.hotel.exception.InvalidRangeDatesException;
import com.alten.cancun.hotel.exception.NoAvailableDatesException;
import com.alten.cancun.hotel.exception.RoomNotFoundException;
import com.alten.cancun.hotel.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class BookService {

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    private final BookRepository bookRepository;

    @Value("${availability.url}")
    private String urlAvailabilityService;

    @Autowired
    public BookService(RestTemplate restTemplate, UserRepository userRepository, RoomRepository roomRepository, BookRepository bookRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookRepository = bookRepository;
    }

    public List<Book> listAllByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for ID=" + userId);
        }

        List<Book> userBookings = bookRepository.findAllByUser(user.get());
        userBookings.sort(Comparator.comparing(Book::getId));

        return userBookings;
    }

    public Book bookRoom(BookVO vo) {
        if (vo.getRoomId() == null
                || vo.getUserId() == null
                || vo.getStartDate() == null
                || vo.getEndDate() == null
                || vo.getStartDate().compareTo(vo.getEndDate()) > 0) {
            throw new IllegalArgumentException();
        }

        if (DAYS.between(vo.getStartDate(), vo.getEndDate()) > 2) {
            throw new InvalidRangeDatesException("It is not possible to book more than 3 days.");
        }

        List<String> datesAvailable = restTemplate.getForObject(urlAvailabilityService + vo.getRoomId(), List.class);
        if (CollectionUtils.isEmpty(datesAvailable) || !checkAvailability(datesAvailable, vo)) {
            throw new NoAvailableDatesException("One or more dates informed is not available.");
        }

        Book book = createBookFromVO(vo);

        return bookRepository.save(book);
    }

    private boolean checkAvailability(List<String> datesAvailable, BookVO vo) {
        LocalDate startDate = vo.getStartDate();
        while (startDate.compareTo(vo.getEndDate()) <= 0) {
            if (!datesAvailable.contains(startDate.toString())) {
                return false;
            }
            startDate = startDate.plusDays(1);
        }

        return true;
    }

    private Book createBookFromVO(BookVO vo) {
        Book book = new Book();

        Optional<User> user = userRepository.findById(vo.getUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for ID=" + vo.getUserId());
        }

        Optional<Room> room = roomRepository.findById(vo.getRoomId());
        if (room.isEmpty()) {
            throw new RoomNotFoundException("Room not found for ID=" + vo.getRoomId());
        }

        book.setUser(user.get());
        book.setRoom(room.get());
        book.setStartDate(vo.getStartDate());
        book.setEndDate(vo.getEndDate());
        book.setCreationDate(LocalDateTime.now());
        book.setActive(true);
        return book;
    }

}
