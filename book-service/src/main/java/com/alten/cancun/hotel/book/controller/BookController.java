package com.alten.cancun.hotel.book.controller;

import com.alten.cancun.hotel.book.controller.vo.BookVO;
import com.alten.cancun.hotel.book.entity.Book;
import com.alten.cancun.hotel.book.service.BookService;
import com.alten.cancun.hotel.mail.Mail;
import com.alten.cancun.hotel.mail.MailService;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    private final MailService mailService;

    @Autowired
    public BookController(BookService bookService, MailService mailService) {
        this.bookService = bookService;
        this.mailService = mailService;
    }

    @GetMapping("/{id}/listAll")
    public ResponseEntity<List<Book>> listAll(
            @ApiParam(value = "User id for filter the bookings", required = true) @PathVariable("id") Long userId) {
        LOGGER.info("List all books");
        return ResponseEntity.status(HttpStatus.OK).body(bookService.listAllByUserId(userId));
    }

    @PostMapping(path = "/")
    public ResponseEntity<Book> bookRoom(@RequestBody BookVO vo) {
        LOGGER.info("Book room: {}", vo);

        Book book = bookService.bookRoom(vo);
        if (StringUtils.isNotBlank(vo.getEmail())) {
            sendEmailToUser(book, vo.getEmail());
        }

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    void sendEmailToUser(Book book, String email) {
        Mail mail = new Mail();
        mail.setFrom("guilherme.serip02@gmail.com");
        mail.setTo(email);
        mail.setSubject("Cancun Hotel - Confirmation of Booking");
        mail.setUser(book.getUser().getName());
        mail.setStartDate(book.getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        mail.setEndDate(book.getEndDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        mailService.sendEmail(mail);
    }

}
