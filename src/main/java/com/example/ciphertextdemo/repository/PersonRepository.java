package com.example.ciphertextdemo.repository;


import com.example.ciphertextdemo.dto.PersonDto;
import com.example.ciphertextdemo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "select id, decrypt_aes(name, 'PusZhVfmRE3BnaFc') as name from person", nativeQuery = true)
    List<Map<String, Object>> findAllByNative();

    @Query(value = "select * from person", nativeQuery = true)
    List<Person> findAllByNativeStar();

    @Query("from Person where id=?1")
    Person findByIdJPA(long id);

    @Query(value = "select new com.example.ciphertextdemo.dto.PersonDto(p.id, p.name)from Person p", nativeQuery = false)
    List<PersonDto> findAllByConstructor();

}
