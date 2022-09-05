package com.example.bookrestcrud.controller;


import com.example.bookrestcrud.dto.BookRequest;
import com.example.bookrestcrud.dto.BookResponse;
import com.example.bookrestcrud.entity.Book;
import com.example.bookrestcrud.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookEndPoint {
    private final BookService bookService;
    private final ModelMapper modelMapper;


    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponse> getById(@PathVariable("id") int id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()) {
            BookResponse map = modelMapper.map(bookOptional.get(), BookResponse.class);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Book> saveBook(@RequestBody BookResponse bookResponse) {
        try {
            Book nwBook = new Book();
            nwBook.setTitle(bookResponse.getTitle());
            nwBook.setIsbn(bookResponse.getIsbn());
            Book book = bookService.addBook(nwBook);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/all")
    public List<BookResponse> getAllBooks() {
        List<Book> bookServiceAll = bookService.getAll();
        List<BookResponse> bookResponses = new ArrayList<>();
        for (Book book : bookServiceAll) {
            bookResponses.add(modelMapper.map(book, BookResponse.class));
        }
        return bookResponses;
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        return bookService.deleteById(id);
    }

    @PutMapping("/upd/{id}")
    public ResponseEntity<Book> updateOfBook(@PathVariable("id") int id, @RequestBody BookRequest bookRequest){

        return bookService.updateBook(id, bookRequest);
    }



}
