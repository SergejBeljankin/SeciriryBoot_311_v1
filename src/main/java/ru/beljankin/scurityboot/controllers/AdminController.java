package ru.beljankin.scurityboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.beljankin.scurityboot.entities.Person;
import ru.beljankin.scurityboot.entities.Role;
import ru.beljankin.scurityboot.services.PersonService;
import ru.beljankin.scurityboot.services.PersonServiseInterface;
import ru.beljankin.scurityboot.services.RoleServise;


import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PersonService personService;
    private final PersonServiseInterface personServiseInterface;
    private final RoleServise roleServise;

    @Autowired
    public AdminController(PersonService personServise, RoleServise roleServise,
                           PersonServiseInterface personServiseInterface){
        this.personService = personServise;
        this.personServiseInterface = personServiseInterface;
        this.roleServise = roleServise;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", personServiseInterface.getAll());
        return "admin/users";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") Person person
            , @RequestParam("rolesNames") String[] rolesNames){

        Set<Role> roleSet = new HashSet<>();
        if(rolesNames.length !=0){
            for (String role: rolesNames) {
                roleSet.add(roleServise.findRoleByString(role));
            }
        } else {
            roleSet.add(roleServise.findRoleByString("ROLE_USER"));
        }
        person.setRoles(roleSet);
        personServiseInterface.save(person);
        return "redirect:/admin";
    }

    @GetMapping("new-person")
    public String newPerson(@ModelAttribute("person") Person person){
        return "admin/new-person";
    }

//
    @PatchMapping("/edit/{id}")
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id,
                         @RequestParam("rolesNames") String[] rolesNames) {
        Set<Role> roleSet = new HashSet<>();
        if(rolesNames.length !=0){
            for (String role: rolesNames) {
                roleSet.add(roleServise.findRoleByString(role));
            }
        } else {
            roleSet.add(roleServise.findRoleByString("ROLE_USER"));
        }
        person.setRoles(roleSet);
        personServiseInterface.update(id, person);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personServiseInterface.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personServiseInterface.select(id));
        return "admin/edit";
    }


    }
