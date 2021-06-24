package com.alten.cancun.hotel.availability.service;

import com.alten.cancun.hotel.availability.entity.Room;
import com.alten.cancun.hotel.availability.repository.RoomRepository;
import com.alten.cancun.hotel.exception.RoomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class AvailabilityService {

    private final RoomRepository roomRepository;

    @Autowired
    public AvailabilityService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Collection<Date> checkAvailability(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()) {
            throw new RoomNotFoundException("Room not found for ID=" + roomId);
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate maximumBookPeriod = currentDate.plusDays(30);

        currentDate = currentDate.plusDays(1);

        return roomRepository.verifyAvailability(roomId, currentDate, maximumBookPeriod);
    }

}
