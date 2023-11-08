package com.example.kafka.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReqDTO {
    private String firstName;
    private String lastName;
    private String email;
    private int social;
}
