package com.alten.cancun.hotel.cancelbook.repository;

import com.alten.cancun.hotel.cancelbook.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
