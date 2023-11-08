package com.example.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.InstantSource;

@Data
@AllArgsConstructor
public class CreditRating {
    private String creditAgencyName;
    private int rating;
}
