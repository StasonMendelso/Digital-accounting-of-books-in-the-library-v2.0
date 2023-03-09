package org.stanislav.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.stanislav.models.Person;

import java.util.List;
import java.util.Optional;

/**
 * @author Stanislav Hlova
 */
@Component
public class PersonDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> readAllPersons() {
        return jdbcTemplate.query("SELECT * FROM Person", new PersonMapper());
    }

    public void create(Person person) {
        jdbcTemplate.update("INSERT INTO Person(full_name, year_of_birth) VALUES(?,?)",
                person.getFullName(),
                person.getYearOfBirth());
    }

    public Person read(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id = ?", new Object[]{id}, new PersonMapper())
                .stream()
                .findAny()
                .orElse(null);
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE Person SET full_name=?, year_of_birth=? WHERE id=?",
                person.getFullName(),
                person.getYearOfBirth(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id = ?", id);
    }

    public Optional<Person> readByFullName(String fullName) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE full_name = ?", new Object[]{fullName}, new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findAny();
    }
}
