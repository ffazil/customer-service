package com.example.demo.data.repository;

import com.example.demo.data.core.AggregateRoot;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoRepositoryBean
public interface AggregateRepository<T extends AggregateRoot> extends JpaRepository<T, UUID> {

    @Override
    @Transactional(readOnly = true)
    @RestResource(exported = false)
    @Query("select e from #{#entityName} e where e.isActive = true")
    List<T> findAll();

    /*@Override
    @Transactional(readOnly = true)
    @Query(value = "select e from #{#entityName} e where e.isActive = true",
            countQuery = "select count(e) from #{#entityName} e where e.isActive = true")
    Page<T> findAll(Pageable pageable);*/

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id in ?1 and e.isActive = true")
    List<T> findAllById(Iterable<UUID> ids);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id = ?1 and e.isActive = true")
    Optional<T> findById(UUID id);

    @Query("select e from #{#entityName} e where e.isActive = false")
    @Transactional(readOnly = true)
    List<T> findInactive();

    @Query("select e from #{#entityName} e")
    @Transactional(readOnly = true)
    List<T> deepFindAll();

    @Override
    @Transactional(readOnly = true)
    @Query("select count(e) from #{#entityName} e where e.isActive = true")
    long count();


    @Override
    @Transactional(readOnly = true)
    default boolean existsById(UUID id) {
        return findById(id)
                .map(t -> t.isActive())
                .orElse(false);
    }

    @Override
    @Modifying
    @Transactional
    @QueryHints(value = {@QueryHint(name = org.hibernate.annotations.QueryHints.FLUSH_MODE, value = "ALWAYS")})
    @Query("update #{#entityName} e set e.isActive=false where e.id = ?1")
    void deleteById(UUID id);

    @Override
    @Transactional
    default void delete(T entity) {
        deleteById(entity.getId());
    }

    @Override
    @Transactional
    default void deleteInBatch(Iterable<T> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }

    @Override
    @Query("update #{#entityName} e set e.isActive=false")
    @Transactional
    @Modifying
    void deleteAll();

    @Modifying
    @Transactional
    @QueryHints(value = {@QueryHint(name = org.hibernate.annotations.QueryHints.FLUSH_MODE, value = "ALWAYS")})
    @Query("delete from #{#entityName} e where e.id = ?1")
    void purgeById(UUID id);

    @Modifying
    @Transactional
    @Query("delete from #{#entityName} e where id in ?1")
    void purgeAll(Collection<UUID> ids);

    @Transactional
    default void purgeAll(Iterable<T> entities) {
        List<UUID> ids = StreamSupport.stream(entities.spliterator(), false)
                .map(entity -> entity.getId())
                .collect(Collectors.toList());

        if (ids == null || ids.isEmpty()) {
        } else {
            purgeAll(ids);
        }


    }

    List<T> findByCreatedBetween(LocalDateTime start, LocalDateTime end);


}
