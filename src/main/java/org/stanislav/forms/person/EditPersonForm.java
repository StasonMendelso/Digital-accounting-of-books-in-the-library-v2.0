package org.stanislav.forms.person;

import org.stanislav.forms.Form;
import org.stanislav.models.Person;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Stanislav Hlova
 */
public class EditPersonForm implements Form<Person> {
    private int id;
    @NotEmpty(message = "ПІБ не може бути пустим.")
    @Size(min = 2, max = 100, message = "Длина ПІБ має бути у межах 2-100 символів.")
    @Pattern(regexp = "^[А-ЯЇЙЄІ][А-яЇЙЄІїйєі'\\-]+ [А-ЯЇЙЄІ][А-яЇЙЄІїйєі'\\-]+ [А-ЯЇЙЄІ][А-яЇЙЄІїйєі'\\-]+$", message = "ПІБ повинен бути у наступному форматі \"Прізвище Ім\'я Побатькові\"")
    private String fullName;
    @Min(value = 1900, message = "Рік народження повинен бути не меншою ніж 1900 рік")
    @Max(value = 2023, message = "Рік народження повинен бути не більшим ніж 2023 рік")
    private int yearOfBirth;

    public EditPersonForm() {
    }

    public EditPersonForm(Person person) {
        this.id = person.getId();
        this.fullName = person.getFullName();
        this.yearOfBirth = person.getYearOfBirth();
    }

    public EditPersonForm(String fullName, int yearOfBirth) {
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Person getModel() {
        Person person = new Person();
        person.setFullName(fullName);
        person.setYearOfBirth(yearOfBirth);
        return person;
    }
}
