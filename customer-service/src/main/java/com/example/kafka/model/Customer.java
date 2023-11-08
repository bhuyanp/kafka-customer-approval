package com.example.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {


    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int social;
    private CUSTOMER_STATUS status;
    private List<CreditRating> ratings;
}

