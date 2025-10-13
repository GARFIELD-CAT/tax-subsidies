package ru.utmn.dyagunov.tax_subsidies.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyJpaRepository;


@Service
public class TaxSubsidiesJpaService {
    TaxSubsidyJpaRepository repository;

    public TaxSubsidiesJpaService(TaxSubsidyJpaRepository repository) {
        this.repository = repository;
    }

    public Iterable<TaxSubsidy> getAll() {
        return repository.findAll();
    }

    public TaxSubsidy getOne(String id){
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
                )
        );
    }

    //    Нужен кеш, чтобы при ретрае случайно не сделать запись с теми же данными
    public TaxSubsidy add(TaxSubsidy taxSubsidy){
        taxSubsidy.setId(null);

        return repository.save(taxSubsidy);
    }

    public TaxSubsidy update(TaxSubsidy taxSubsidy){
        if (!repository.existsById(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", taxSubsidy.getId())
            );

        return repository.save(taxSubsidy);
    }

    public void delete(String id){
        if (!repository.existsById(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        repository.deleteById(id);
    }
}
