package com.example.kafka.dto;


import com.example.kafka.model.CUSTOMER_STATUS;
import com.example.kafka.model.CreditRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private CUSTOMER_STATUS status;
    private List<CreditRating> ratings;
}
