package com.example.demo.data.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor(force = true)
public abstract class BroadcastEvent<T extends Serializable> {

    private final UUID id;
    private final LocalDateTime publicationDate;
    private final T data;

    protected BroadcastEvent(UUID id, LocalDateTime publicationDate, T data) {
        this.id = id;
        this.publicationDate = publicationDate;
        this.data = data;
    }

    /*@JsonCreator
    public BroadcastEvent(UUID id, LocalDateTime publicationDate, Class<?> aggregateType, T data) {
        this.id = id;
        this.publicationDate = publicationDate;
        this.aggregateType = aggregateType;
        this.data = data;
    }*/

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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BroadcastEvent)) return false;
        final BroadcastEvent<?> other = (BroadcastEvent<?>) o;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        return this$id == null ? other$id == null : this$id.equals(other$id);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BroadcastEvent;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
