package com.example.bookrestcrud.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookrestcrud.dto.BookRequest;
import com.example.bookrestcrud.dto.BookResponse;
import com.example.bookrestcrud.entity.Book;
import com.example.bookrestcrud.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BookServiceImp.class})
@ExtendWith(SpringExtension.class)
class BookServiceImpTest {
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImp bookServiceImp;

    @MockBean
    private ModelMapper modelMapper;


    @Test
    void testAddBook() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        when(bookRepository.save((Book) any())).thenReturn(book);

        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("Isbn");
        book1.setTitle("Dr");
        assertSame(book, bookServiceImp.addBook(book1));
        verify(bookRepository).save((Book) any());
    }


    @Test
    void testGetAll() {
        ArrayList<Book> bookList = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> actualAll = bookServiceImp.getAll();
        assertSame(bookList, actualAll);
        assertTrue(actualAll.isEmpty());
        verify(bookRepository).findAll();
    }


    @Test
    void testFindByTitle() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        when(bookRepository.findByTitle((String) any())).thenReturn(book);
        assertSame(book, bookServiceImp.findByTitle("Dr"));
        verify(bookRepository).findByTitle((String) any());
    }


    @Test
    void testFindById() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        Optional<Book> ofResult = Optional.of(book);
        when(bookRepository.findById((Integer) any())).thenReturn(ofResult);
        Optional<Book> actualFindByIdResult = bookServiceImp.findById(1);
        assertSame(ofResult, actualFindByIdResult);
        assertTrue(actualFindByIdResult.isPresent());
        verify(bookRepository).findById((Integer) any());
    }

    @Test
    void testDeleteById() {
        doNothing().when(bookRepository).deleteById((Integer) any());
        when(bookRepository.existsById((Integer) any())).thenReturn(true);
        ResponseEntity actualDeleteByIdResult = bookServiceImp.deleteById(1);
        assertNull(actualDeleteByIdResult.getBody());
        assertEquals(HttpStatus.OK, actualDeleteByIdResult.getStatusCode());
        assertTrue(actualDeleteByIdResult.getHeaders().isEmpty());
        verify(bookRepository).existsById((Integer) any());
        verify(bookRepository).deleteById((Integer) any());
    }


    @Test
    void testDeleteById2() {
        doNothing().when(bookRepository).deleteById((Integer) any());
        when(bookRepository.existsById((Integer) any())).thenReturn(false);
        ResponseEntity actualDeleteByIdResult = bookServiceImp.deleteById(1);
        assertNull(actualDeleteByIdResult.getBody());
        assertEquals(HttpStatus.NOT_FOUND, actualDeleteByIdResult.getStatusCode());
        assertTrue(actualDeleteByIdResult.getHeaders().isEmpty());
        verify(bookRepository).existsById((Integer) any());
    }


    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        Optional<Book> ofResult = Optional.of(book);

        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("Isbn");
        book1.setTitle("Dr");
        when(bookRepository.save((Book) any())).thenReturn(book1);
        when(bookRepository.existsById((Integer) any())).thenReturn(true);
        when(bookRepository.findById((Integer) any())).thenReturn(ofResult);

        BookResponse bookResponse = new BookResponse();
        bookResponse.setIsbn("Isbn");
        bookResponse.setTitle("Dr");
        when(modelMapper.map((Object) any(), (Class<BookResponse>) any())).thenReturn(bookResponse);
        ResponseEntity actualUpdateBookResult = bookServiceImp.updateBook(1, new BookRequest("Dr", "Isbn"));
        assertTrue(actualUpdateBookResult.hasBody());
        assertTrue(actualUpdateBookResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualUpdateBookResult.getStatusCode());
        verify(bookRepository).existsById((Integer) any());
        verify(bookRepository).save((Book) any());
        verify(bookRepository).findById((Integer) any());
        verify(modelMapper).map((Object) any(), (Class<BookResponse>) any());
    }


    @Test
    void testUpdateBook2() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        Optional<Book> ofResult = Optional.of(book);

        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("Isbn");
        book1.setTitle("Dr");
        when(bookRepository.save((Book) any())).thenReturn(book1);
        when(bookRepository.existsById((Integer) any())).thenReturn(false);
        when(bookRepository.findById((Integer) any())).thenReturn(ofResult);

        BookResponse bookResponse = new BookResponse();
        bookResponse.setIsbn("Isbn");
        bookResponse.setTitle("Dr");
        when(modelMapper.map((Object) any(), (Class<BookResponse>) any())).thenReturn(bookResponse);
        ResponseEntity actualUpdateBookResult = bookServiceImp.updateBook(1, new BookRequest("Dr", "Isbn"));
        assertTrue(actualUpdateBookResult.hasBody());
        assertTrue(actualUpdateBookResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualUpdateBookResult.getStatusCode());
        verify(bookRepository).existsById((Integer) any());
        verify(bookRepository).save((Book) any());
        verify(bookRepository).findById((Integer) any());
        verify(modelMapper).map((Object) any(), (Class<BookResponse>) any());
    }

    @Test
    void testUpdateBook3() {

        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        when(bookRepository.save((Book) any())).thenReturn(book);
        when(bookRepository.existsById((Integer) any())).thenReturn(true);
        when(bookRepository.findById((Integer) any())).thenReturn(null);

        BookResponse bookResponse = new BookResponse();
        bookResponse.setIsbn("Isbn");
        bookResponse.setTitle("Dr");
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        when(modelMapper.map((Object) any(), (Class<BookResponse>) any())).thenReturn(bookResponse);
        bookServiceImp.updateBook(1, new BookRequest("Dr", "Isbn"));
    }
}

