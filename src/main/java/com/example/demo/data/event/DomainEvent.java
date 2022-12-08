package com.example.demo.data.event;

import com.example.demo.data.core.AggregateRoot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@ToString
public abstract class DomainEvent<T extends Serializable> implements Event {

    private final UUID id = UUID.randomUUID();
    private final LocalDateTime publicationDate = LocalDateTime.now();
    private final Class<?> aggregateType;
    private final T data;

    protected DomainEvent(T data) {

        if (ClassUtils.isAssignable(AggregateRoot.class, data.getClass())) {
            this.data = SerializationUtils.clone(data);
        } else {
            this.data = data;
        }


        this.aggregateType = ResolvableType //
                .forClass(DomainEvent.class, this.getClass()) //
                .resolveGeneric(0);
    }



    /*public DomainEvent() {
        this.aggregateType = null;
        this.data = null;
    }*/


    @Override
    public Class<?> getAggregateType() {
        return aggregateType;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public UUID getId() {
        return this.id;
    }

    public LocalDateTime getPublicationDate() {
        return this.publicationDate;
    }

    public T getData() {
        return this.data;
    }

    @JsonIgnore
    public String getGlobalTopic() {

        String[] eventNamesSplit = StringUtils.splitByWholeSeparator(getClass().getSimpleName(), "$");

        String topicBuilder = "interplay" +
                "." +
                "enterprise" +
                "." +
                "global" +
                "." +
                getAggregateType().getSimpleName().toLowerCase() +
                //topicBuilder.append(this.data.getClass().getSimpleName().toLowerCase());
                "." +
                eventNamesSplit[0].toLowerCase();

        return topicBuilder;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final DomainEvent<?> other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        return Objects.equals(this$id, other$id);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DomainEvent;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
