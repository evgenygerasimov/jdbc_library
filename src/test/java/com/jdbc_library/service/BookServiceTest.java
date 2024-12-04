package com.jdbc_library.service;

import com.jdbc_library.entity.Book;
import com.jdbc_library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);

        book1 = new Book(1L, "Book 1", "Author 1", 1894);
        book2 = new Book(2L, "Book 2", "Author 2", 1988);
    }

    @Test
    void shouldReturnAllBooksTest() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        assertEquals("Book 2", books.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnBookByIdWhenBookExistsTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals("Book 1", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenBookByIdDoesNotExistTest() {
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(3L);

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(3L);
    }

    @Test
    void shouldCallSaveWhenAddBookTest() {
        bookService.addBook(book1);

        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void shouldReturnTrueWhenUpdateBookAndBookExistsTest() {
        Book updatedBook = new Book(null, "Updated Book", "Updated Author", 1987);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        boolean result = bookService.updateBook(1L, updatedBook);

        assertTrue(result);
        verify(bookRepository, times(1)).update(updatedBook);
        assertEquals(1L, updatedBook.getId());
    }

    @Test
    void shouldReturnFalseWhenUpdateBookAndBookDoesNotExistTest() {
        Book updatedBook = new Book(null, "Updated Book", "Updated Author", 1983);
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());

        boolean result = bookService.updateBook(3L, updatedBook);

        assertFalse(result);
        verify(bookRepository, never()).update(updatedBook);
    }

    @Test
    void shouldReturnTrueWhenDeleteBookAndBookExistsTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        boolean result = bookService.deleteBook(1L);

        assertTrue(result);
        verify(bookRepository, times(1)).delete(1L);
    }

    @Test
    void shouldReturnFalseWhenDeleteBookAndBookDoesNotExistTest() {
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());

        boolean result = bookService.deleteBook(3L);

        assertFalse(result);
        verify(bookRepository, never()).delete(3L);
    }
}
