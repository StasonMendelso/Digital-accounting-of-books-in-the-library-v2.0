package org.stanislav.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stanislav.models.Book;
import org.stanislav.models.Person;
import org.stanislav.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Stanislav Hlova
 */
@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Person findByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBookList());

            person.get().getBookList().forEach(book -> {
                Date takenAt = book.getTakenAt();
                if (takenAt != null && Math.abs(takenAt.getTime() - new Date().getTime()) > 864000000) {
                    book.setExpired(true);
                }
            });

            return person.get().getBookList();
        }
        return Collections.emptyList();
    }
}
