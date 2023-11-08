package com.example.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditReportEvent {
    private String id;
    private String creditAgency;
    private int creditScore;
}
