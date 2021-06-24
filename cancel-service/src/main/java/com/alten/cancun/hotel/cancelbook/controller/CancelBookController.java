package com.alten.cancun.hotel.cancelbook.controller;

import com.alten.cancun.hotel.cancelbook.service.CancelBookService;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cancelbook")
public class CancelBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelBookController.class);

    private final CancelBookService cancelBookService;

    @Autowired
    public CancelBookController(CancelBookService cancelBookService) {
        this.cancelBookService = cancelBookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity cancelBookRoom(
            @ApiParam(value = "Book ID required to cancel", required = true) @PathVariable("id") Long bookId) {
        cancelBookService.cancelBookRoom(bookId);
        return ResponseEntity.status(HttpStatus.OK).body("Book canceled successfully.");
    }

}
