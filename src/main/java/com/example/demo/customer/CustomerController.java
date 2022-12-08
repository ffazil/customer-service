package com.example.demo.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RepositoryRestController
public class CustomerController {

    private final CustomerService customerService;

    /*@GetMapping(path = "/customers")
    public ResponseEntity<?> findAll() {
        List<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }*/

    @GetMapping(path = "/customers/find")
    public ResponseEntity<?> findByFirstname(@RequestParam("firstname") String firstname) {
        Optional<Customer> customer = customerService.findByFirstname(firstname);
        return customer.map(existing -> ResponseEntity.ok(existing))
                .orElse(ResponseEntity.notFound().build());
    }
}
