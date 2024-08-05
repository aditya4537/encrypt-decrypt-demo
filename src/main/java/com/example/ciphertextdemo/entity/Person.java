package com.example.ciphertextdemo.entity;

import com.example.ciphertextdemo.utils.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "name")
    private String name;

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
