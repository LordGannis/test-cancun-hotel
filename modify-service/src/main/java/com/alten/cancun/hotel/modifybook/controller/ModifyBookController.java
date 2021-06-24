package com.alten.cancun.hotel.modifybook.controller;

import com.alten.cancun.hotel.modifybook.controller.vo.BookVO;
import com.alten.cancun.hotel.modifybook.entity.Book;
import com.alten.cancun.hotel.modifybook.service.ModifyBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modifybook")
public class ModifyBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyBookController.class);

    private final ModifyBookService bookService;

    @Autowired
    public ModifyBookController(ModifyBookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/")
    public ResponseEntity<Book> modifyBookRoom(@RequestBody BookVO vo) {
        LOGGER.info("Modify Book room: {}", vo);
        return ResponseEntity.status(HttpStatus.OK).body(bookService.modifyBook(vo));
    }

}
