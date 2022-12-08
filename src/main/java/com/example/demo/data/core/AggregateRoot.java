package com.example.demo.data.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author firoz
 * @since 17/09/17
 */

@Slf4j
@Getter
@MappedSuperclass
public abstract class AggregateRoot<A extends AggregateRoot<A>> extends AbstractAggregateRoot {

    /**
     * All domain events currently captured by the aggregate.
     */
    @JsonIgnore
    @Getter(onMethod = @__(@DomainEvents)) //
    private transient final List<Object> domainEvents = new ArrayList<>();

    protected AggregateRoot() {
        super(null);
    }

    /**
     * Registers the given event object for publication on a call to a Spring Data repository's save method.
     *
     * @param event must not be {@literal null}.
     * @return
     */
    protected <T> T registerEvent(T event) {

        Assert.notNull(event, "Domain event must not be null!");

        this.domainEvents.add(event);
        return event;
    }

    /**
     * Clears all domain events currently held. Usually invoked by the infrastructure in place in Spring Data
     * repositories.
     */
    @AfterDomainEventPublication
    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }

    }

    protected final A andEventsFrom(A aggregate) {

        Assert.notNull(aggregate, "Aggregate must not be null!");

        this.domainEvents.addAll(aggregate.getDomainEvents());

        return (A) this;
    }

    protected final A andEvent(Object event) {

        registerEvent(event);

        return (A) this;
    }

    public A emit() {
        return andEvent(Emitted.of(this));
    }
}
