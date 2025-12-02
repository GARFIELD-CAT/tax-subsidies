package ru.utmn.dyagunov.tax_subsidies.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;


public interface TaxSubsidiesServiceInterface {

    Iterable<TaxSubsidy> getAll();

    Page<TaxSubsidy> getAll(Pageable pageable);

    TaxSubsidy getOne(String id);

    TaxSubsidy add(TaxSubsidy taxSubsidy);

    TaxSubsidy update(TaxSubsidy taxSubsidy);

    void delete(String id);

    Double getAverageObservationValue();

    Iterable<TaxSubsidy> findByFilter(String referenceArea, String measure, String unitOfMeasure, Integer timePeriod);
}
