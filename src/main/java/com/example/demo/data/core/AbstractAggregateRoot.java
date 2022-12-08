package com.example.demo.data.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author firoz
 */

@Getter
@MappedSuperclass
@ToString(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class AbstractAggregateRoot implements Persistable<UUID>, Serializable {

    @CreatedDate
    @Column(columnDefinition = "DATETIME")
    protected LocalDateTime created = LocalDateTime.now();
    @Id
    @Getter
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    @Version
    @JsonIgnore
    private Long version;
    @Getter
    @JsonIgnore
    @Column(name = "is_active")
    private boolean isActive = true;
    @Transient
    private boolean isNew = true;
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime modified;

    @LastModifiedBy
    private String lastModifiedBy;

    protected AbstractAggregateRoot(UUID givenId) {
        this.id = givenId != null ? givenId : UUID.randomUUID();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    @JsonIgnore
    public boolean isNew() {
        return isNew;
    }

    /**
     * Marks the entity as not new not make sure we merge entity instances instead of trying to persist them.
     */
    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj.getClass().equals(this.getClass()))) {
            return false;
        }

        AbstractAggregateRoot that = (AbstractAggregateRoot) obj;

        return ObjectUtils.nullSafeEquals(this.getId(), that.getId());
    }

    protected Boolean terminate() {
        if (this.isActive) {
            this.isActive = false;
        }
        return isActive;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
