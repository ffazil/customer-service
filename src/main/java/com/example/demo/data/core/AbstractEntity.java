package com.example.demo.data.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * @author firoz
 */

@MappedSuperclass
@Getter
@ToString
@EqualsAndHashCode
public class AbstractEntity implements Persistable<Long>, Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;

    @Version
    @JsonIgnore
    private Long version;

    @Transient
    private boolean isNew = true;

    protected AbstractEntity() {
        this.id = 0L;
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

        AbstractEntity that = (AbstractEntity) obj;

        return ObjectUtils.nullSafeEquals(this.getId(), that.getId());
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
