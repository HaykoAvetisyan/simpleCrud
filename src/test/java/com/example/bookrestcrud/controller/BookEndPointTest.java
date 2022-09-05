package com.example.bookrestcrud.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.example.bookrestcrud.dto.BookRequest;
import com.example.bookrestcrud.dto.BookResponse;
import com.example.bookrestcrud.entity.Book;
import com.example.bookrestcrud.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {BookEndPoint.class})
@ExtendWith(SpringExtension.class)
class BookEndPointTest {
    @Autowired
    private BookEndPoint bookEndPoint;

    @MockBean
    private BookService bookService;

    @MockBean
    private ModelMapper modelMapper;

    /**
     * Method under test: {@link BookEndPoint#delete(int)}
     */
    @Test
    void testDelete() throws Exception {
        when(bookService.deleteById(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/delete/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookEndPoint).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link BookEndPoint#getAllBooks()}
     */
    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.getAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/all");
        MockMvcBuilders.standaloneSetup(bookEndPoint)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link BookEndPoint#getAllBooks()}
     */
    @Test
    void testGetAllBooks2() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("?");
        book.setTitle("Dr");

        ArrayList<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(bookService.getAll()).thenReturn(bookList);

        BookResponse bookResponse = new BookResponse();
        bookResponse.setIsbn("Isbn");
        bookResponse.setTitle("Dr");
        when(modelMapper.map((Object) any(), (Class<BookResponse>) any())).thenReturn(bookResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/all");
        MockMvcBuilders.standaloneSetup(bookEndPoint)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"title\":\"Dr\",\"isbn\":\"Isbn\"}]"));
    }

    /**
     * Method under test: {@link BookEndPoint#getById(int)}
     */
    @Test
    void testGetById() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        Optional<Book> ofResult = Optional.of(book);
        when(bookService.findById(anyInt())).thenReturn(ofResult);

        BookResponse bookResponse = new BookResponse();
        bookResponse.setIsbn("Isbn");
        bookResponse.setTitle("Dr");
        when(modelMapper.map((Object) any(), (Class<BookResponse>) any())).thenReturn(bookResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/{id}", 1);
        MockMvcBuilders.standaloneSetup(bookEndPoint)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"title\":\"Dr\",\"isbn\":\"Isbn\"}"));
    }

    /**
     * Method under test: {@link BookEndPoint#getById(int)}
     */
    @Test
    void testGetById2() throws Exception {
        when(bookService.findById(anyInt())).thenReturn(Optional.empty());

        BookResponse bookResponse = new BookResponse();
        bookResponse.setIsbn("Isbn");
        bookResponse.setTitle("Dr");
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        when(modelMapper.map((Object) any(), (Class<BookResponse>) any())).thenReturn(bookResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookEndPoint).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    /**
     * Method under test: {@link BookEndPoint#saveBook(BookResponse)}
     */
    @Test
    void testSaveBook() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("Isbn");
        book.setTitle("Dr");
        when(bookService.addBook((Book) any())).thenReturn(book);

        BookResponse bookResponse = new BookResponse();
        bookResponse.setIsbn("Isbn");
        bookResponse.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(bookResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(bookEndPoint)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"title\":\"Dr\",\"isbn\":\"Isbn\"}"));
    }

    /**
     * Method under test: {@link BookEndPoint#updateOfBook(int, BookRequest)}
     */
    @Test
    void testUpdateOfBook() throws Exception {
        when(bookService.updateBook(anyInt(), (BookRequest) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        BookRequest bookRequest = new BookRequest();
        bookRequest.setIsbn("Isbn");
        bookRequest.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(bookRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/upd/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookEndPoint).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }
}

