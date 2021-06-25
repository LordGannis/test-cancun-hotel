package com.alten.cancun.hotel.modifybook.controller;

import com.alten.cancun.hotel.mail.Mail;
import com.alten.cancun.hotel.mail.MailService;
import com.alten.cancun.hotel.modifybook.controller.vo.BookVO;
import com.alten.cancun.hotel.modifybook.entity.Book;
import com.alten.cancun.hotel.modifybook.service.ModifyBookService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/modifybook")
public class ModifyBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyBookController.class);

    private final ModifyBookService modifyBookService;

    private final MailService mailService;

    @Autowired
    public ModifyBookController(ModifyBookService modifyBookService, MailService mailService) {
        this.modifyBookService = modifyBookService;
        this.mailService = mailService;
    }

    @PutMapping("/")
    public ResponseEntity<Book> modifyBookRoom(@RequestBody BookVO vo) {
        LOGGER.info("Modify Book room: {}", vo);

        Book book = modifyBookService.modifyBook(vo);
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
