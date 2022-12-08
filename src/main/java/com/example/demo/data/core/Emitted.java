package com.example.demo.data.core;

import com.example.demo.data.event.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class Emitted extends DomainEvent<AggregateRoot> {

    public Emitted(AggregateRoot data) {
        super(data);
    }

    public static Emitted of(AggregateRoot aggregateRoot) {
        return new Emitted(aggregateRoot);
    }

    public AggregateRoot getAggregateRoot() {
        return getData();
    }
}