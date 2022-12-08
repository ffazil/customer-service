package com.example.demo.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCustomerService implements CustomerService{

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    @Override
    public Optional<Customer> findByFirstname(String firstname) {
        Optional<Customer> customer = customerRepository.findByFirstname(firstname);
        return customer;
    }
}
