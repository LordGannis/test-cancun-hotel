package com.alten.cancun.hotel.modifybook.service;

import com.alten.cancun.hotel.exception.BookNotFoundException;
import com.alten.cancun.hotel.exception.InvalidRangeDatesException;
import com.alten.cancun.hotel.exception.NoAvailableDatesException;
import com.alten.cancun.hotel.exception.RoomNotFoundException;
import com.alten.cancun.hotel.exception.UserNotFoundException;
import com.alten.cancun.hotel.modifybook.controller.vo.BookVO;
import com.alten.cancun.hotel.modifybook.entity.Book;
import com.alten.cancun.hotel.modifybook.entity.Room;
import com.alten.cancun.hotel.modifybook.entity.User;
import com.alten.cancun.hotel.modifybook.repository.BookRepository;
import com.alten.cancun.hotel.modifybook.repository.RoomRepository;
import com.alten.cancun.hotel.modifybook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ModifyBookService {

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    private final BookRepository bookRepository;

    @Value("${availability.url}")
    private String urlAvailabilityService;

    @Autowired
    public ModifyBookService(RestTemplate restTemplate, UserRepository userRepository, RoomRepository roomRepository, BookRepository bookRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookRepository = bookRepository;
    }

    public Book modifyBook(BookVO vo) {
        Book book = validateParameters(vo);

        changeBookFromVO(vo, book);

        return bookRepository.save(book);
    }

    private Book validateParameters(BookVO vo) {
        if (vo.getBookId() == null
                || vo.getRoomId() == null
                || vo.getUserId() == null
                || vo.getStartDate() == null
                || vo.getEndDate() == null
                || vo.getStartDate().compareTo(vo.getEndDate()) > 0) {
            throw new IllegalArgumentException();
        }

        Optional<Book> optionalBook = bookRepository.findById(vo.getBookId());
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found for ID=" + vo.getBookId());
        }

        if (DAYS.between(vo.getStartDate(), vo.getEndDate()) > 2) {
            throw new InvalidRangeDatesException("It is not possible to book more than 3 days.");
        }

        List<String> datesAvailable = restTemplate.getForObject(urlAvailabilityService + vo.getRoomId(), List.class);
        LocalDate startDate = optionalBook.get().getStartDate();
        while (startDate.compareTo(optionalBook.get().getEndDate()) <= 0) {
            datesAvailable.add(startDate.toString());
            startDate = startDate.plusDays(1);
        }
        if (CollectionUtils.isEmpty(datesAvailable) || !checkAvailability(datesAvailable, vo)) {
            throw new NoAvailableDatesException("One or more dates informed is not available.");
        }

        return optionalBook.get();
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

    private void changeBookFromVO(BookVO vo, Book book) {
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
        book.setModificationDate(LocalDateTime.now());
    }

}
