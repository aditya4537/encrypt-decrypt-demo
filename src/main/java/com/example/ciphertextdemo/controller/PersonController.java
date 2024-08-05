package com.example.ciphertextdemo.controller;

import com.example.ciphertextdemo.dto.PersonDto;
import com.example.ciphertextdemo.entity.Person;
import com.example.ciphertextdemo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/add")
    public Person createPerson(@RequestBody Person person) {
        return personService.save(person);
    }

    @GetMapping("/getAll")
    public List<Person> getAllPersons() {
        return personService.findAllDecrypted();
    }

    @GetMapping("/getAllByNative")
    public List<Map<String, Object>> getAllPersonsByNative() {
        return personService.findAllByNative();
    }

    @GetMapping("/getAllByNativeStar")
    public List<Person> findAllByNativeStar() {
        return personService.findAllByNativeStar();
    }

    @GetMapping("/getAllByConstructor")
    public List<PersonDto> findAllByConstructor() {
        return personService.findAllByConstructor();
    }

    @GetMapping("/get/{id}")
    public Optional<Person> getPersonById(@PathVariable Long id) {
        return personService.findByIdDecrypted(id);
    }

    @GetMapping("/get-jpa/{id}")
    public Person getPersonByIdJPA(@PathVariable Long id) {
        return personService.findByIdJPA(id);
    }
}
