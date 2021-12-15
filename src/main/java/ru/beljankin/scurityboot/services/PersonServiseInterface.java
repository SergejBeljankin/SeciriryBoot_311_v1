package ru.beljankin.scurityboot.services;

import ru.beljankin.scurityboot.entities.Person;

import java.util.List;

public interface PersonServiseInterface {
    List<Person> getAll();
    Person select(long id);
    void save(Person person);
    void delete(long id);
    void update(long id, Person person);
    Person findByUserName(String username);
}
