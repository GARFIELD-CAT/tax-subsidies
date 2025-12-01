package ru.utmn.dyagunov.tax_subsidies.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface CommonRepository<T> {

    T save(T domain);

    Iterable<T> save(Collection<T> domains);

    void delete(String id);

    void delete(T domain);

    T findById(String id);

    Iterable<T> findAll();

    Page<T> findAll(Pageable pageable);

    boolean exists(String id);

    long count();
}

