package com.example.ciphertextdemo.service;


import com.example.ciphertextdemo.dto.PersonDto;
import com.example.ciphertextdemo.entity.Person;
import com.example.ciphertextdemo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;


    @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Transactional(readOnly = true)
    public List<Person> findAllDecrypted() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Person> findByIdDecrypted(Long id) {
        return personRepository.findById(id);
    }

    public List<Map<String, Object>> findAllByNative(){
        return personRepository.findAllByNative();
    }

    public List<Person> findAllByNativeStar(){
        return personRepository.findAllByNativeStar();
    }

    public Person findByIdJPA(long id){
        return personRepository.findByIdJPA(id);
    }

    public List<PersonDto> findAllByConstructor(){
        return personRepository.findAllByConstructor();
    }
}
