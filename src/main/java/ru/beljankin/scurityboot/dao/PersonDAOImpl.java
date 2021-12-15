package ru.beljankin.scurityboot.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.beljankin.scurityboot.entities.Person;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class PersonDAOImpl implements PersonDAO {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Person> getAll(){
        return entityManager.createQuery("from Person", Person.class).getResultList();
    }


    @Override
    public Person select(long id){
        return entityManager.find(Person.class, id);
    }

    @Override
    public void save(Person person){
        entityManager.persist(person);
    }

    @Override
    public void delete(long id){
        entityManager.remove(select(id));
    }

    @Override
    public void update(long id, Person personVariable){
        Person person = select(id);
        person.setUsername(personVariable.getUsername());
        person.setPassword(personVariable.getPassword());//
//        person.setPassword();//
        person.setSurname(personVariable.getSurname());
        person.setName(personVariable.getName());
        person.setRoles(personVariable.getRoles());
        save(person);
    }



    @Override
    public List<Person> findPersonByRole(String roleName) {
        return entityManager.createQuery("select person from Person person inner join Role role on person.id = role.id where role.name = :roleName", Person.class)
                .setParameter("roleName", roleName).getResultList();
    }

//    @Override
//    public void setRoles(Set<Role> roleSet) {
//    }
    public Person findByUserName(String username){
        return entityManager.createQuery("select person from Person as person where person.username =:username", Person.class)
                .setParameter("username", username).getSingleResult();
    }
}
