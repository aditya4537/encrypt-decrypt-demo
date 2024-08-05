package com.example.ciphertextdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDto {
    private Long id;
    private String name;

    public PersonDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
