package com.alten.cancun.hotel.cancelbook.service;

import com.alten.cancun.hotel.cancelbook.entity.Book;
import com.alten.cancun.hotel.cancelbook.repository.BookRepository;
import com.alten.cancun.hotel.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CancelBookService {

    private final BookRepository bookRepository;

    @Autowired
    public CancelBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void cancelBookRoom(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found for ID=" + bookId);
        }

        Book book = optionalBook.get();
        book.setActive(false);

        bookRepository.save(book);
    }

}
