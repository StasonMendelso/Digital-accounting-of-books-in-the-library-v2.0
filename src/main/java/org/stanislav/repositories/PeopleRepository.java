package org.stanislav.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stanislav.models.Person;

/**
 * @author Stanislav Hlova
 */
@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {
    Person findByFullName(String fullName);
}
