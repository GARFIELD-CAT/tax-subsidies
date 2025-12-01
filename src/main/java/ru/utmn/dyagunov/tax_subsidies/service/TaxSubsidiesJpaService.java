package ru.utmn.dyagunov.tax_subsidies.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.CommonRepository;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyJpaRepository;

import java.util.Collection;
import java.util.stream.StreamSupport;


@Service
@Profile("JpaEngine")
public class TaxSubsidiesJpaService implements TaxSubsidiesServiceInterface {
    TaxSubsidyJpaRepository repository;

    public TaxSubsidiesJpaService(
            TaxSubsidyJpaRepository repository,
            @Qualifier("CsvRepository") CommonRepository<TaxSubsidy> repository2
    ) {
        this.repository = repository;

        if (repository.count() == 0 && repository2.count() > 0) {
            Iterable<TaxSubsidy> all = repository2.findAll();
            Collection<TaxSubsidy> collection = StreamSupport.stream(all.spliterator(), false).toList();
            repository.saveAll(collection);
        }
    }

    public Iterable<TaxSubsidy> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<TaxSubsidy> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public TaxSubsidy getOne(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
                )
        );
    }

    //    Нужен кеш, чтобы при ретрае случайно не сделать запись с теми же данными
    public TaxSubsidy add(TaxSubsidy taxSubsidy) {
        return repository.save(taxSubsidy);
    }

    public TaxSubsidy update(TaxSubsidy taxSubsidy) {
        if (!repository.existsById(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", taxSubsidy.getId())
            );

        return repository.save(taxSubsidy);
    }

    public void delete(String id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        repository.deleteById(id);
    }

    public Float getAverageObservationValue(){
        return repository.getAverageObservationValue();
    }
}
