package com.example.demo.customer;

import com.example.demo.data.repository.AggregateRepository;

import java.util.Optional;

public interface CustomerRepository extends AggregateRepository<Customer> {

    Optional<Customer> findByFirstname(String firstname);
}
