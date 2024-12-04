package com.jdbc_library.controller;

import com.jdbc_library.entity.Book;
import com.jdbc_library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return new ResponseEntity<>("Book added successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) {
        boolean updated = bookService.updateBook(id, book);
        if (updated) {
            return new ResponseEntity<>("Book updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
    }
}
