package com.alten.cancun.hotel.availability.repository;

import com.alten.cancun.hotel.availability.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "select date_trunc('day', available_date)::::date " +
            "from generate_series (:inicioPeriodo, :fimPeriodo, '1 day'::::interval) available_date " +
            "where available_date not in (select booked_dates from tb_book book, " +
            "                   generate_series (book.startdate, book.enddate, '1 day'::::interval) booked_dates" +
            "                   where book.room_id = :roomId" +
            "                   and book.active is true)",
            nativeQuery = true)
    List<Date> verifyAvailability(@Param("roomId") Long roomId,
                                  @Param("inicioPeriodo") LocalDate inicioPeriodo,
                                  @Param("fimPeriodo") LocalDate fimPeriodo);

}
