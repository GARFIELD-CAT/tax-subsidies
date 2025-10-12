package ru.utmn.dyagunov.tax_subsidies.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyCsvRepository;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyJdbcRepository;
import java.util.Collection;
import java.util.stream.StreamSupport;


@Service
public class TaxSubsidiesService {
    TaxSubsidyCsvRepository repository;
    TaxSubsidyJdbcRepository repository2;

    public TaxSubsidiesService(TaxSubsidyCsvRepository repository, TaxSubsidyJdbcRepository repository2) {
        this.repository = repository;
        this.repository2 = repository2;

        if (repository2.count() == 0 && repository.count() > 0) {
            Iterable<TaxSubsidy> all = repository.findAll();
            Collection<TaxSubsidy> collection = StreamSupport.stream(all.spliterator(), false).toList();
            repository2.save(collection);
        }
    }

    public Iterable<TaxSubsidy> getAll() {
        return repository2.findAll();
    }

    public TaxSubsidy getOne(String id){
        if (!repository2.exists(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        return repository2.findById(id);
    }

//    Нужен кеш, чтобы при ретрае случайно не сделать запись с теми же данными
    public TaxSubsidy add(TaxSubsidy taxSubsidy){
        if (repository2.exists(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, String.format("Запись с id=%s создана ранее", taxSubsidy.getId())
            );

        repository2.save(taxSubsidy);

        return taxSubsidy;
    }

    public TaxSubsidy update(TaxSubsidy taxSubsidy){
        if (!repository2.exists(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", taxSubsidy.getId())
            );

        repository2.save(taxSubsidy);

        return taxSubsidy;
    }

    public void delete(String id){
        if (!repository2.exists(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        repository2.delete(id);
    }
}
