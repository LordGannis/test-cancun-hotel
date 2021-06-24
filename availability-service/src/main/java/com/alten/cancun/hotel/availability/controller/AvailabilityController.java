package com.alten.cancun.hotel.availability.controller;

import com.alten.cancun.hotel.availability.service.AvailabilityService;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityController.class);

    private final AvailabilityService service;

    public AvailabilityController(AvailabilityService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity checkAvailability(
            @ApiParam(value = "Room ID to check availability", required = true) @PathVariable("id") Long roomId) {
        LOGGER.info("Availability checkAvailability - id={}", roomId);
        return ResponseEntity.status(HttpStatus.OK).body(service.checkAvailability(roomId));
    }

}
