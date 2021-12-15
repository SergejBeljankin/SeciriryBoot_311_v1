package ru.beljankin.scurityboot.repostories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.beljankin.scurityboot.entities.Person;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByUsername(String username);
}
