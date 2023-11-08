package com.example.kafka.service;

import com.example.kafka.dto.CreditReportEvent;
import com.example.kafka.dto.CustomerRegistrationEvent;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExperianService {
    public static final String AGENCY = "Experian";
    private final KafkaTemplate<String, CreditReportEvent> kafkaTemplate;

    @KafkaListener(topics = {"customer-registration"}, groupId = "experian-grp")
    public void processRegistrationEvent(CustomerRegistrationEvent customerRegistrationEvent) throws InterruptedException {
        log.info("Event received: {}", Json.pretty(customerRegistrationEvent));
        //Artificially adding delay to simulate real scenario
        long delay = RandomUtils.nextLong(1000, 6000);
        log.info("Artifical pause:{}ms",delay);
        Thread.sleep(delay);
        int score = RandomUtils.nextInt(540, 800);
        log.info("Experian Credit Score for {} : {}",customerRegistrationEvent.getId(),score);
        log.info("Sending credit report back to customer service");

        kafkaTemplate.send("credit-report",
                customerRegistrationEvent.getId(),
                new CreditReportEvent(customerRegistrationEvent.getId(), AGENCY, score));
    }
}
