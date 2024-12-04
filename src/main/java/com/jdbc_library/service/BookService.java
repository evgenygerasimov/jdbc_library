package com.jdbc_library.service;


import com.jdbc_library.entity.Book;
import com.jdbc_library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public boolean updateBook(Long id, Book book) {
        if (bookRepository.findById(id).isPresent()) {
            book.setId(id);
            bookRepository.update(book);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }
}
