package org.stanislav.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stanislav.models.Book;
import org.stanislav.models.Person;
import org.stanislav.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Stanislav Hlova
 */
@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getAllBooks(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        }
        return booksRepository.findAll();
    }

    public List<Book> getPaginatedBooks(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by(Sort.Order.asc("year")))).getContent();
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book getBook(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.ifPresent(value -> value.setOwner(null));
    }

    @Transactional
    public void assign(int id, Person person) {
        Optional<Book> book = booksRepository.findById(id);
        book.ifPresent(value -> value.setOwner(person));
    }
}
