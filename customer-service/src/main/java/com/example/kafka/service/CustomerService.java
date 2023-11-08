package com.example.kafka.service;


import com.example.kafka.dto.CreditReportEvent;
import com.example.kafka.dto.CustomerRegistrationEvent;
import com.example.kafka.dto.CustomerReqDTO;
import com.example.kafka.dto.CustomerResDTO;
import com.example.kafka.model.CUSTOMER_STATUS;
import com.example.kafka.model.CreditRating;
import com.example.kafka.model.Customer;
import com.example.kafka.repo.CustomerRepo;
import io.swagger.v3.core.util.Json;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final KafkaTemplate<String, CustomerRegistrationEvent> kafkaTemplate;


    @KafkaListener(topics = "credit-report", groupId = "credit-report-recipient-grp")
    public void consumeCreditReportEvent(CreditReportEvent creditReportEvent) {
        log.info("Credit report received: {}", Json.pretty(creditReportEvent));

        updateCreditScore(creditReportEvent);
    }

    public synchronized void updateCreditScore(CreditReportEvent creditReportEvent) {
        customerRepo.findById(creditReportEvent.getId())
                .ifPresent(customer -> {
                    if (CollectionUtils.isEmpty(customer.getRatings())) {
                        customer.setRatings(List.of(
                                new CreditRating(creditReportEvent.getCreditAgency(), creditReportEvent.getCreditScore())
                        ));

                    } else {
                        Collections.addAll(customer.getRatings(),
                                new CreditRating(creditReportEvent.getCreditAgency(), creditReportEvent.getCreditScore()));
                    }
                    if (creditReportEvent.getCreditScore() < 550) {
                        if (customer.getStatus() != CUSTOMER_STATUS.DECLINED) {
                            customer.setStatus(CUSTOMER_STATUS.DECLINED);
                        }
                    } else {
                        //if already approved/declined dont approve
                        if (customer.getStatus() == CUSTOMER_STATUS.NEW) {
                            customer.setStatus(CUSTOMER_STATUS.APPROVED);
                        }
                    }
                    customerRepo.save(customer);
                });
    }

    public CustomerResDTO addCustomer(CustomerReqDTO customerReqDTO) {
        Customer customer = customerRepo.save(Customer
                .builder()
                .email(customerReqDTO.getEmail())
                .firstName(customerReqDTO.getFirstName())
                .lastName(customerReqDTO.getLastName())
                .email(customerReqDTO.getEmail())
                .social(customerReqDTO.getSocial())
                .status(CUSTOMER_STATUS.NEW)
                .build()
        );
        kafkaTemplate.send("customer-registration", customer.getId(),
                new CustomerRegistrationEvent(customer.getId(), customerReqDTO));
        return getCustomerResDTO(customer);
    }


    @Transactional(readOnly = true)
    public Optional<CustomerResDTO> getCustomerById(String id) {
        return customerRepo.findById(id).map(this::getCustomerResDTOWithCreditDetails);
    }

    @Transactional(readOnly = true)
    public List<CustomerResDTO> getCustomers() {
        return customerRepo.findAll().stream().map(this::getCustomerResDTO).toList();
    }


    private CustomerResDTO getCustomerResDTO(Customer customer) {
        return CustomerResDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getFirstName())
                .status(customer.getStatus())
                .build();
    }

    private CustomerResDTO getCustomerResDTOWithCreditDetails(Customer customer) {
        CustomerResDTO customerResDTO = getCustomerResDTO(customer);
        customerResDTO.setRatings(customer.getRatings());
        return customerResDTO;
    }

}
