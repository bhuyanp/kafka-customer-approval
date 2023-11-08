package com.example.kafka.controller;

import com.example.kafka.dto.CustomerReqDTO;
import com.example.kafka.dto.CustomerResDTO;
import com.example.kafka.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;




    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResDTO> getCustomer(@PathVariable String id){
         return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerResDTO> getCustomers(){
        return customerService.getCustomers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerResDTO saveCustomer(@RequestBody CustomerReqDTO customerReqDTO){
        return customerService.addCustomer(customerReqDTO);
    }



}
