package org.stanislav.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stanislav.models.Book;
import org.stanislav.models.Person;

import java.util.List;

/**
 * @author Stanislav Hlova
 */
@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {
    List<Book> findAllByOwner(Person owner);

}
