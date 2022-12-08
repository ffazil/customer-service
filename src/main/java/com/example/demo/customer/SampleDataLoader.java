package com.example.demo.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleDataLoader implements ApplicationRunner {

    private final CustomerRepository customerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(customerRepository.count() == 0){
            Customer suraj = new Customer("Suraj", "Mallenahalli");
            suraj = customerRepository.save(suraj);
        }
    }
}
