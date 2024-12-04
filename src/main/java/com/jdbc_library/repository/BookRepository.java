package com.jdbc_library.repository;

import com.jdbc_library.entity.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> rowMapper = (rs, rowNum) -> new Book(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getInt("publicationYear")
    );

    public int save(Book book) {
        return jdbcTemplate.update(
                "INSERT INTO book (title, author, publicationYear) VALUES (?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getPublicationYear()
        );
    }

    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", rowMapper);
    }

    public Optional<Book> findById(Long id) {
        List<Book> books = jdbcTemplate.query("SELECT * FROM book WHERE id = ?", rowMapper, id);
        return books.stream().findFirst();
    }

    public int update(Book book) {
        return jdbcTemplate.update(
                "UPDATE book SET title = ?, author = ?, publicationYear = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId()
        );
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }
}
