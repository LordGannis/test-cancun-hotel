package com.alten.cancun.hotel.book.repository;

import com.alten.cancun.hotel.book.entity.Book;
import com.alten.cancun.hotel.book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByUser(User user);

}
