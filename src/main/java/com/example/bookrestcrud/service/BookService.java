package com.example.bookrestcrud.service;

import com.example.bookrestcrud.dto.BookRequest;
import com.example.bookrestcrud.entity.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book addBook(Book book);

    List<Book> getAll();

    Book findByTitle(String title);

    Optional<Book> findById(int id);

   ResponseEntity<Book> deleteById(int id);

    ResponseEntity<Book> updateBook(int id, BookRequest bookRequest);
}
