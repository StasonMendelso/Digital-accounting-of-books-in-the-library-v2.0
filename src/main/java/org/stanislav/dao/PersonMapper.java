package org.stanislav.dao;

import org.springframework.jdbc.core.RowMapper;
import org.stanislav.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Stanislav Hlova
 */
public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setFullName(resultSet.getString("full_name"));
        person.setYearOfBirth(resultSet.getInt("year_of_birth"));
        return person;
    }
}
