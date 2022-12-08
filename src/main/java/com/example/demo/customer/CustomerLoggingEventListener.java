package com.example.demo.customer;

import com.example.demo.data.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerLoggingEventListener {

    @EventListener
    public void log(DomainEvent<Customer> event){
        Customer customer = event.getData();
        log.info("Added customer: {}", customer);
    }
}
