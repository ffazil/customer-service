package com.example.demo.data.event;

import com.example.demo.data.core.AggregateRoot;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString(callSuper = false)
public class LifecycleEvent<T extends AggregateRoot> implements Event {

    private final UUID id = UUID.randomUUID();
    private final LocalDateTime publicationDate = LocalDateTime.now();
    private final Class<?> aggregateType;
    private final T aggregate;
    private final Status status;

    public LifecycleEvent(T aggregate, Status status) {
        this.aggregate = aggregate;
        /*this.aggregateType = ResolvableType //
                .forClass(LifecycleEvent.class, this.getClass()) //
                .resolveGeneric(0);*/
        this.aggregateType = aggregate.getClass();
        if (status == null) {
            if (this.aggregate.isNew()) {
                this.status = Status.NEW;
            } else {
                this.status = Status.UPDATED;
            }
        } else {
            this.status = status;
        }


    }

    public static <T extends AggregateRoot> LifecycleEvent from(T aggregate) {
        return new LifecycleEvent(aggregate, null);
    }

    public static <T extends AggregateRoot> LifecycleEvent deleted(T aggregate) {
        return new LifecycleEvent(aggregate, Status.DELETED);
    }

    public String getAggregateFullname() {
        return this.aggregate.getClass().getName().toLowerCase();
    }

    public enum Status {
        NEW,
        UPDATED,
        DELETED
    }
}
