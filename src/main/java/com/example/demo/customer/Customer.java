package com.example.demo.customer;

import com.example.demo.data.core.AggregateRoot;
import com.example.demo.data.event.DomainEvent;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Getter
@Entity
@NoArgsConstructor(force = true)
public class Customer extends AggregateRoot<Customer> {

    private String firstname;

    private String lastname;

    public Customer(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;

        // Register Created event
        this.registerEvent(Created.of(this));
    }


    /**
     * Definition of Created event.
     */
    @Value
    @EqualsAndHashCode(callSuper = true)
    public static class Created extends DomainEvent<Customer> {


        protected Created(Customer customer) {
            super(customer);
        }

        public static Created of(Customer customer) {
            return new Created(customer);
        }

        public Customer getCustomer() {
            return this.getData();
        }
    }
}
