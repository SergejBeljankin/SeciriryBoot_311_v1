package ru.beljankin.scurityboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.beljankin.scurityboot.entities.Person;
import ru.beljankin.scurityboot.services.PersonService;


import java.security.Principal;

@RestController
public class MainController {
    private PersonService personService;

    @Autowired
    public void setUserService(PersonService personService){
        this.personService = personService;
    }


    @GetMapping("/")
    public String homePage(){
        return "home";
    }
    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers(Principal principal){
        Person person = personService.findByUsername(principal.getName());
        return  "Защищено от доступа <br>рады вас приветствовать: " + person.getUsername() + " " + person.getEmail();
    }

    @GetMapping("/read_profile")
    public String pageForReadProfile(){
        return "read profile page";
    }

    @GetMapping("/only_for_admins")
    public String pageOnlyForAdmins(){
        return "admins page";
    }
}
