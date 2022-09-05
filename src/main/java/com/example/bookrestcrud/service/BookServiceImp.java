package com.example.bookrestcrud.service;


import com.example.bookrestcrud.dto.BookRequest;
import com.example.bookrestcrud.dto.BookResponse;
import com.example.bookrestcrud.entity.Book;
import com.example.bookrestcrud.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAll() {

        return bookRepository.findAll();
    }

    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }


    public ResponseEntity deleteById(int id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity updateBook(int id, BookRequest bookRequest) {
        if (!bookRepository.existsById(id)) {
            ResponseEntity.notFound().build();
        }
        Optional<Book> byId = bookRepository.findById(id);
        Book book = byId.get();
        if(bookRequest.getTitle()!=null){
            book.setTitle(bookRequest.getTitle());
        }
        if(bookRequest.getIsbn()!=null){
            book.setIsbn(bookRequest.getIsbn());
        }
        bookRepository.save(book);
        BookResponse bookResponse = modelMapper.map(book, BookResponse.class);
        return ResponseEntity.ok(bookResponse);
    }
}
