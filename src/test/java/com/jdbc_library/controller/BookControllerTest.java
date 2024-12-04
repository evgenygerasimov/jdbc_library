package com.jdbc_library.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdbc_library.entity.Book;
import com.jdbc_library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddBookTest() throws Exception {
        Book book = new Book(1L, "New Book", "New Author", 1976);

        doNothing().when(bookService).addBook(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Book added successfully"));

        verify(bookService, times(1)).addBook(book);
    }

    @Test
    void shouldReturnAllBooksTest() throws Exception {
        Book book1 = new Book(1L, "Book 1", "Author 1", 1993);
        Book book2 = new Book(2L, "Book 2", "Author 2", 1993);

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void shouldReturnBookByIdWhenBookExistsTest() throws Exception {
        Book book = new Book(1L, "Book 1", "Author 1", 1987);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book 1"))
                .andExpect(jsonPath("$.author").value("Author 1"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void shouldReturnNotFoundWhenBookByIdDoesNotExistTest() throws Exception {
        when(bookService.getBookById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(3L);
    }

    @Test
    void shouldUpdateBookWhenBookExistsTest() throws Exception {
        Book updatedBook = new Book(null, "Updated Book", "Updated Author", 1987);

        when(bookService.updateBook(1L, updatedBook)).thenReturn(true);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book updated successfully"));

        verify(bookService, times(1)).updateBook(1L, updatedBook);
    }

    @Test
    void shouldReturnNotFoundWhenUpdateBookAndBookDoesNotExistTest() throws Exception {
        Book updatedBook = new Book(null, "Updated Book", "Updated Author", 1978);

        when(bookService.updateBook(3L, updatedBook)).thenReturn(false);

        mockMvc.perform(put("/api/books/3")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).updateBook(3L, updatedBook);
    }

    @Test
    void shouldDeleteBookWhenBookExistsTest() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted successfully"));

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeleteBookAndBookDoesNotExistTest() throws Exception {
        when(bookService.deleteBook(3L)).thenReturn(false);

        mockMvc.perform(delete("/api/books/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));

        verify(bookService, times(1)).deleteBook(3L);
    }
}
