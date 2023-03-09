package org.stanislav.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.stanislav.dao.PersonDao;
import org.stanislav.models.Person;

/**
 * @author Stanislav Hlova
 */
@Component
public class PersonValidator implements Validator {
    private final PersonDao personDao;

    @Autowired
    public PersonValidator(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personDao.readByFullName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "","Користувач з таким ПІБ вже є. Виберіть інший.");
        }
    }
}
