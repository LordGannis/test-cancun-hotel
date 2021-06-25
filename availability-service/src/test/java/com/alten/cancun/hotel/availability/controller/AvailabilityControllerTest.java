package com.alten.cancun.hotel.availability.controller;

import com.alten.cancun.hotel.availability.AvailabilityServiceApplication;
import com.alten.cancun.hotel.availability.entity.Room;
import com.alten.cancun.hotel.availability.repository.RoomRepository;
import com.alten.cancun.hotel.availability.service.AvailabilityService;
import com.alten.cancun.hotel.exception.RoomNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@SpringBootTest(classes = {AvailabilityServiceApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AvailabilityControllerTest {

    private RoomRepository roomRepository;

    private AvailabilityController availabilityController;

    @BeforeEach
    public void beforeRunTests() {
        roomRepository = Mockito.mock(RoomRepository.class);
        AvailabilityService availabilityService = new AvailabilityService(roomRepository);
        availabilityController = new AvailabilityController(availabilityService);
    }

    @Test
    public void whenServiceHasNoValidRoom_thenRoomNotFoundExceptionIsThrown() {
        Exception exception = Assertions.assertThrows(RoomNotFoundException.class, () -> {
            Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
            availabilityController.checkAvailability(10L);
        });

        String expectedMessage = "Room not found for ID=10";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenAvailabilityReturnNoDate_thenMethodReturnEmptyList() {
        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Room()));
        Mockito.when(roomRepository.verifyAvailability(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

        ResponseEntity<Collection> responseEntity = availabilityController.checkAvailability(10L);

        Assertions.assertTrue(responseEntity.getBody().isEmpty());
    }

    @Test
    public void whenAvailabilityReturnDates_thenMethodReturnNonEmptyList() {
        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Room()));
        Mockito.when(roomRepository.verifyAvailability(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(new Date()));

        ResponseEntity<Collection> responseEntity = availabilityController.checkAvailability(10L);

        Assertions.assertTrue(!responseEntity.getBody().isEmpty());
    }

}
