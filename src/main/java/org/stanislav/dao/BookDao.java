package org.stanislav.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.stanislav.models.Book;
import org.stanislav.models.Person;

import java.util.List;
import java.util.Optional;

/**
 * @author Stanislav Hlova
 */
@Component
public class BookDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> readByOwnerId(int ownerId) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?", new Object[]{ownerId}, new BeanPropertyRowMapper<>(Book.class));
    }

    public List<Book> readAll() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book read(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream()
                .findAny()
                .orElse(null);
    }

    public void create(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, \"year\") VALUES(?,?,?)",
                book.getTitle(),
                book.getAuthor(),
                book.getYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET author=?,title=?,\"year\"=? WHERE id=?",
                book.getAuthor(),
                book.getTitle(),
                book.getYear(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?",
                id);
    }

    public Optional<Person> readBookOwner(int id) {
        return jdbcTemplate.query("SELECT * FROM Book JOIN Person ON book.person_id = person.id WHERE book.id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findAny();
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?",
                id);
    }

    public void assign(int id, Person person) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?",
                person.getId(),
                id);
    }
}
