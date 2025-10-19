package ru.utmn.dyagunov.tax_subsidies.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.CommonRepository;

import java.util.Collection;
import java.util.stream.StreamSupport;


@Service
@Profile({"CsvEngine", "JdbcEngine"})
public class TaxSubsidiesService implements TaxSubsidiesServiceInterface {
    CommonRepository<TaxSubsidy> repository;

    public TaxSubsidiesService(
            CommonRepository<TaxSubsidy> repository,
            @Qualifier("CsvRepository") CommonRepository<TaxSubsidy> repository2
    ) {
        this.repository = repository;

        if (repository2.getClass().equals(repository.getClass())) {
            return;
        }

        if (repository.count() == 0 && repository2.count() > 0) {
            Iterable<TaxSubsidy> all = repository2.findAll();
            Collection<TaxSubsidy> collection = StreamSupport.stream(all.spliterator(), false).toList();
            repository.save(collection);
        }
    }

    public Iterable<TaxSubsidy> getAll() {
        return repository.findAll();
    }

    public TaxSubsidy getOne(String id) {
        if (!repository.exists(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        return repository.findById(id);
    }

    //    Нужен кеш, чтобы при ретрае случайно не сделать запись с теми же данными
    public TaxSubsidy add(TaxSubsidy taxSubsidy) {
        if (repository.exists(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, String.format("Запись с id=%s создана ранее", taxSubsidy.getId())
            );

        repository.save(taxSubsidy);

        return taxSubsidy;
    }

    public TaxSubsidy update(TaxSubsidy taxSubsidy) {
        if (!repository.exists(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", taxSubsidy.getId())
            );

        repository.save(taxSubsidy);

        return taxSubsidy;
    }

    public void delete(String id) {
        if (!repository.exists(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        repository.delete(id);
    }
}
