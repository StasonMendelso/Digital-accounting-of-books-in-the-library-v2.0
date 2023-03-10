package org.stanislav.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.stanislav.dao.PersonDao;
import org.stanislav.models.Person;
import org.stanislav.services.PeopleService;

/**
 * @author Stanislav Hlova
 */
@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.findByFullName(person.getFullName()) != null) {
            errors.rejectValue("fullName", "", "Користувач з таким ПІБ вже є. Виберіть інший.");
        }
    }
}
